package net.customware.http.dispatch.test.server.guice;

import net.customware.http.dispatch.server.BatchActionHandler;
import net.customware.http.dispatch.server.guice.ActionHandlerModule;
import net.customware.http.dispatch.server.secure.SecureSessionValidator;
import net.customware.http.dispatch.shared.BatchAction;
import net.customware.http.dispatch.test.server.handler.CauseErrorHandler;
import net.customware.http.dispatch.test.server.handler.Counter;
import net.customware.http.dispatch.test.server.handler.IncrementCounterHandler;
import net.customware.http.dispatch.test.server.handler.PingActionHandler;
import net.customware.http.dispatch.test.server.handler.ResetCounterHandler;
import net.customware.http.dispatch.test.server.secure.PingActionJBossSecureSessionValidator;
import net.customware.http.dispatch.test.shared.CauseError;
import net.customware.http.dispatch.test.shared.IncrementCounter;
import net.customware.http.dispatch.test.shared.PingAction;
import net.customware.http.dispatch.test.shared.ResetCounter;

/**
 * Module which binds the handlers and configurations
 * 
 */
public class ServerModule extends ActionHandlerModule
{

	@Override
	protected void configureHandlers()
	{
		bind(SecureSessionValidator.class)
				.to(PingActionJBossSecureSessionValidator.class);

		bindHandler(PingAction.class, PingActionHandler.class);

		bindHandler(BatchAction.class, BatchActionHandler.class);
		bindHandler(CauseError.class, CauseErrorHandler.class);
		bindHandler(IncrementCounter.class,
				IncrementCounterHandlerWithNoArgsConstructor.class);
		bindHandler(ResetCounter.class,
				ResetCounterHandlerWithNoArgsConstructor.class);
	}

	static class CounterHolder
	{
		static Counter counter = new Counter();
	}

	static class IncrementCounterHandlerWithNoArgsConstructor extends
			IncrementCounterHandler
	{
		public IncrementCounterHandlerWithNoArgsConstructor()
		{
			super(CounterHolder.counter);
		}
	}

	static class ResetCounterHandlerWithNoArgsConstructor extends
			ResetCounterHandler
	{
		public ResetCounterHandlerWithNoArgsConstructor()
		{
			super(CounterHolder.counter);
		}
	}
}