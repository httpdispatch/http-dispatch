package net.customware.http.dispatch.client.guice;

import net.customware.http.dispatch.client.ExceptionHandler;

import com.google.inject.AbstractModule;

/**
 * Abstract base class that binds an instance of {@link ExceptionHandler} for
 * use
 * by {@link net.customware.http.dispatch.client.AbstractDispatchAsync}
 * implementations.
 * 
 * @author David Peterson, Eugene Popovich
 */
public class AbstractDispatchModule extends AbstractModule
{
	protected final Class<? extends ExceptionHandler> exceptionHandlerType;

	public AbstractDispatchModule(
			Class<? extends ExceptionHandler> exceptionHandlerType)
	{
		this.exceptionHandlerType = exceptionHandlerType;
	}

	@Override
	protected void configure()
	{
		bind(ExceptionHandler.class).to(exceptionHandlerType);
	}
}
