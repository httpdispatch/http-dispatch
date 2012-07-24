package net.customware.http.dispatch.test.client.standard;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;
import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.client.ExceptionHandler;

public abstract class AbstractTestCase extends TestCase
{
	protected static final int TEST_DELAY = 15000;

	private CountDownLatch lock = new CountDownLatch(1);
	private Throwable ex;
	public static AbstractTestCase testCase;

	@Override
	protected void setUp() throws Exception
	{
		testCase = AbstractTestCase.this;
		super.setUp();
	}

	public Throwable getEx()
	{
		return ex;
	}

	public void setEx(Throwable ex)
	{
		this.ex = ex;
	}

	public static class CustomExceptionHandler implements ExceptionHandler
	{
		@Override
		public Status onFailure(Throwable e)
		{
			testCase.ex = e;
			throw new RuntimeException(e);
		}
	}

	public abstract class TestCallback<T> implements AsyncCallback<T>
	{
		public TestCallback()
		{
			ex = null;
		}

		@Override
		public void onFailure(Throwable e)
		{
			ex = e;
			finishTest();
		}
	}

	public void delayTestFinish()
	{
		try
		{
			lock.await(TEST_DELAY, TimeUnit.MILLISECONDS);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		if (ex != null)
		{
			throw new RuntimeException(ex);
		}
	}

	public void finishTest()
	{
		lock.countDown();
	}

}
