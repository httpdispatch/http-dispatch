package net.customware.http.dispatch.test.client.guice;

import junit.framework.Assert;
import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.client.DispatchAsync;
import net.customware.http.dispatch.client.ExceptionHandler;
import net.customware.http.dispatch.client.guice.SecureDispatchModule;
import net.customware.http.dispatch.client.secure.CookieSecureSessionAccessor;
import net.customware.http.dispatch.client.secure.JreBasicSecureDispatchServiceAsync;
import net.customware.http.dispatch.shared.ActionException;
import net.customware.http.dispatch.shared.BatchAction;
import net.customware.http.dispatch.shared.BatchAction.OnException;
import net.customware.http.dispatch.shared.BatchResult;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.test.client.standard.AbstractTestCase;
import net.customware.http.dispatch.test.shared.CauseError;
import net.customware.http.dispatch.test.shared.IncrementCounter;
import net.customware.http.dispatch.test.shared.IncrementCounterResult;
import net.customware.http.dispatch.test.shared.PingAction;
import net.customware.http.dispatch.test.shared.PingActionResult;
import net.customware.http.dispatch.test.shared.ResetCounter;
import net.customware.http.dispatch.test.shared.ResetCounterResult;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceTestSecureDispatcher extends AbstractTestCase
{
	// protected static final String DISPATCH_URL =
	// "http://localhost:8080/HTTP_Dispatch_Test_Server/guice_secure_dispatch";
	protected static final String DISPATCH_URL =
			"http://httpdispatch-ep.rhcloud.com/guice_secure_dispatch";

	static DispatchAsync dispatch;

	static
	{
		Injector injector = Guice.createInjector(
				new SecureDispatchModule(CustomExceptionHandler.class,
						CustomSecureDispatchServiceAsync.class,
						JBossCookieSecureSessionAccessor.class)
				);
		dispatch = injector.getInstance(DispatchAsync.class);
	}

	private static class CustomExceptionHandler implements ExceptionHandler
	{
		@Override
		public Status onFailure(Throwable e)
		{
			testCase.setEx(e);
			return Status.CONTINUE;
		}
	}

	private static class JBossCookieSecureSessionAccessor extends
			CookieSecureSessionAccessor
	{

		public JBossCookieSecureSessionAccessor()
		{
			super("JSESSIONID");
		}

	}

	private static class CustomSecureDispatchServiceAsync extends
			JreBasicSecureDispatchServiceAsync
	{
		public CustomSecureDispatchServiceAsync()
		{
			super(DISPATCH_URL);
		}
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testIncrementCounter()
	{
		dispatch.execute(new ResetCounter(),
				new TestCallback<ResetCounterResult>()
				{
					@Override
					public void onSuccess(ResetCounterResult result)
					{
						finishTest();
					}
				});
		delayTestFinish();
		dispatch.execute(new IncrementCounter(1),
				new TestCallback<IncrementCounterResult>()
				{
					@Override
					public void onSuccess(
							IncrementCounterResult result)
					{
						assertEquals(1, result.getCurrent());
						finishTest();
					}
				});
		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish();
	}

	public void testBatchAction()
	{

		BatchAction batch = new BatchAction(OnException.ROLLBACK,
				new ResetCounter(), new IncrementCounter(1),
				new IncrementCounter(2));

		dispatch.execute(batch, new TestCallback<BatchResult>()
		{
			@Override
			public void onSuccess(BatchResult result)
			{
				assertEquals(3, result.size());

				assertTrue(result.getResult(0) instanceof ResetCounterResult);
				assertEquals(0, result.getResult(0, ResetCounterResult.class)
						.getNewValue());

				assertTrue(result.getResult(1) instanceof IncrementCounterResult);
				assertEquals(1,
						result.getResult(1, IncrementCounterResult.class)
								.getAmount());
				assertEquals(1,
						result.getResult(1, IncrementCounterResult.class)
								.getCurrent());

				assertTrue(result.getResult(2) instanceof IncrementCounterResult);
				assertEquals(2,
						result.getResult(2, IncrementCounterResult.class)
								.getAmount());
				assertEquals(3,
						result.getResult(2, IncrementCounterResult.class)
								.getCurrent());

				finishTest();
			}
		});

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish();
	}

	public void testIncrementCounterSequence()
	{

		dispatch.execute(new ResetCounter(),
				new TestCallback<ResetCounterResult>()
				{
					@Override
					public void onSuccess(ResetCounterResult result)
					{
						dispatch.execute(new IncrementCounter(1),
								new TestCallback<IncrementCounterResult>()
								{
									@Override
									public void onSuccess(
											IncrementCounterResult result)
									{
										assertEquals(1, result.getCurrent());

										dispatch.execute(
												new IncrementCounter(2),
												new AsyncCallback<IncrementCounterResult>()
												{
													@Override
													public void onFailure(
															Throwable caught)
													{
														throw new RuntimeException(
																caught);
													}

													@Override
													public void onSuccess(
															IncrementCounterResult result)
													{
														assertEquals(3, result
																.getCurrent());
														finishTest();
													}
												});
									}
								});
					}
				});

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish();
	}

	/**
	 * Verifies that if there is more than one error within a
	 * {@linkplain BatchAction}, with an {@linkplain OnException#CONTINUE
	 * continue} policy, all exceptions are returned.
	 */
	public void testBatchActionRetrieveExceptions()
	{

		BatchAction action = new BatchAction(OnException.CONTINUE,
				new CauseError(), new CauseError());

		dispatch.execute(action, new TestCallback<BatchResult>()
		{
			@Override
			public void onSuccess(BatchResult result)
			{
				assertEquals(2, result.getExceptions().size());

				for (DispatchException exception : result.getExceptions())
					assertTrue(exception instanceof ActionException);

				finishTest();
			}
		});

		delayTestFinish();
	}

	/**
	 * Verifies that if there are both successful and failed executions for a
	 * {@linkplain BatchAction} with
	 * an {@linkplain OnException#CONTINUE} policy, results and exceptions are
	 * returned as expected.
	 */
	public void testBatchActionExceptionAndResult()
	{

		BatchAction action = new BatchAction(OnException.CONTINUE,
				new CauseError(), new IncrementCounter(1));

		dispatch.execute(action, new TestCallback<BatchResult>()
		{
			@Override
			public void onSuccess(BatchResult result)
			{
				assertEquals(2, result.getExceptions().size());
				assertTrue(result.getException(0) instanceof ActionException);
				assertNull(result.getException(1)); // second action succeeds

				assertNull(result.getResult(0));
				assertNotNull(result.getResult(1));

				finishTest();

			}
		});

		delayTestFinish();
	}

	@Test
	public void testBasicStringPing()
	{
		final String testObject = "Test String Object";
		PingAction pingRequest = new PingAction(testObject);
		dispatch.execute(pingRequest, new TestCallback<PingActionResult>()
		{
			@Override
			public void onSuccess(PingActionResult result)
			{
				Assert.assertNotNull(result);
				Assert.assertTrue(result instanceof PingActionResult);
				PingActionResult prr = (PingActionResult) result;
				Assert.assertEquals(testObject, prr.get());

				finishTest();

			}
		});

		delayTestFinish();
	}

	@Test
	public void testNullSubObject()
	{
		String testObject = null;
		PingAction pingRequest = new PingAction(testObject);
		dispatch.execute(pingRequest, new TestCallback<PingActionResult>()
		{
			@Override
			public void onSuccess(PingActionResult result)
			{
				Assert.assertNotNull(result);
				Assert.assertTrue(result instanceof PingActionResult);
				PingActionResult prr = (PingActionResult) result;
				Assert.assertNull(prr.get());

				finishTest();

			}
		});

		delayTestFinish();
	}

	@Test
	public void testNullResult()
	{
		PingAction pingRequest = new PingAction(true, false);
		dispatch.execute(pingRequest, new TestCallback<PingActionResult>()
		{
			@Override
			public void onSuccess(PingActionResult result)
			{
				Assert.assertNull(result);

				finishTest();

			}
		});

		delayTestFinish();
	}

	public void testExceptionResult() throws Throwable
	{
		PingAction pingRequest = new PingAction(false, true);

		dispatch.execute(pingRequest, new TestCallback<PingActionResult>()
		{
			@Override
			public void onSuccess(PingActionResult result)
			{
				Assert.assertNull(result);

				finishTest();

			}

			@Override
			public void onFailure(Throwable e)
			{
				Assert.assertTrue(e instanceof DispatchException);
				DispatchException de = (DispatchException) e;
				de.printStackTrace();
				setEx(null);
				finishTest();
			}
		});
		delayTestFinish();
	}

	public void testBatchActionExceptionAndRollback()
	{

		BatchAction action = new BatchAction(OnException.ROLLBACK,
				new PingAction("Test"), new PingAction(false, true));

		dispatch.execute(action, new TestCallback<BatchResult>()
		{
			@Override
			public void onSuccess(BatchResult result)
			{
				Assert.assertNull(result);

				finishTest();

			}

			@Override
			public void onFailure(Throwable e)
			{
				Assert.assertTrue(e instanceof DispatchException);
				DispatchException de = (DispatchException) e;
				de.printStackTrace();
				setEx(null);
				finishTest();
			}
		});

		delayTestFinish();
	}
}
