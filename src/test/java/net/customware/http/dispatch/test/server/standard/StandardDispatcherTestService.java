package net.customware.http.dispatch.test.server.standard;

import net.customware.http.dispatch.server.BatchActionHandler;
import net.customware.http.dispatch.server.DefaultActionHandlerRegistry;
import net.customware.http.dispatch.server.Dispatch;
import net.customware.http.dispatch.server.InstanceActionHandlerRegistry;
import net.customware.http.dispatch.server.SimpleDispatch;
import net.customware.http.dispatch.server.standard.AbstractStandardDispatchServlet;
import net.customware.http.dispatch.test.server.handler.CauseErrorHandler;
import net.customware.http.dispatch.test.server.handler.Counter;
import net.customware.http.dispatch.test.server.handler.IncrementCounterHandler;
import net.customware.http.dispatch.test.server.handler.PingActionHandler;
import net.customware.http.dispatch.test.server.handler.ResetCounterHandler;

/**
 */
public class StandardDispatcherTestService extends
		AbstractStandardDispatchServlet
{

	private static final long serialVersionUID = 1L;

	private Dispatch dispatch;

	public StandardDispatcherTestService()
	{
		// Setup for test case
		Counter counter = new Counter();

		InstanceActionHandlerRegistry registry = new DefaultActionHandlerRegistry();
		registry.addHandler(new IncrementCounterHandler(counter));
		registry.addHandler(new ResetCounterHandler(counter));
		registry.addHandler(new CauseErrorHandler());
		registry.addHandler(new BatchActionHandler());
		registry.addHandler(new PingActionHandler());
		dispatch = new SimpleDispatch(registry);
	}

	@Override
	protected Dispatch getDispatch()
	{
		return dispatch;
	}
}
