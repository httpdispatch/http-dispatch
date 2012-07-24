package net.customware.http.dispatch.test.client.standard;

import junit.framework.Assert;
import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.client.standard.JreBasicStandardDispatchServiceAsync;
import net.customware.http.dispatch.client.standard.StandardDispatchAsync;
import net.customware.http.dispatch.shared.ActionException;
import net.customware.http.dispatch.shared.BatchAction;
import net.customware.http.dispatch.shared.BatchAction.OnException;
import net.customware.http.dispatch.shared.BatchResult;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.test.shared.CauseError;
import net.customware.http.dispatch.test.shared.IncrementCounter;
import net.customware.http.dispatch.test.shared.IncrementCounterResult;
import net.customware.http.dispatch.test.shared.PingAction;
import net.customware.http.dispatch.test.shared.PingActionResult;
import net.customware.http.dispatch.test.shared.ResetCounter;
import net.customware.http.dispatch.test.shared.ResetCounterResult;

import org.junit.Test;

public class TestStandardDispatcher extends AbstractTestCase
{
	// protected static final String DISPATCH_URL =
	// "http://localhost:8080/HTTP_Dispatch_Test_Server/standard_dispatch";
	protected static final String DISPATCH_URL =
			"http://httpdispatch-ep.rhcloud.com/standard_dispatch";
	private static StandardDispatchAsync dispatch;

	static
	{
		dispatch = new StandardDispatchAsync(new CustomExceptionHandler(),
				new JreBasicStandardDispatchServiceAsync(DISPATCH_URL));
	}

	@Override
	protected void setUp() throws Exception
	{

		super.setUp();
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
}
