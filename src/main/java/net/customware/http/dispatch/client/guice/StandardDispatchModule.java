package net.customware.http.dispatch.client.guice;

import net.customware.http.dispatch.client.DefaultExceptionHandler;
import net.customware.http.dispatch.client.DispatchAsync;
import net.customware.http.dispatch.client.ExceptionHandler;
import net.customware.http.dispatch.client.standard.StandardDispatchAsync;
import net.customware.http.dispatch.client.standard.StandardDispatchServiceAsync;

import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * This module binds the {@link DispatchAsync} to {@link StandardDispatchAsync}.
 * For simple cases, just add this as a \@GinModule in your {@link Ginjector}
 * instance.
 * <p/>
 * If you want to provide a custom {@link ExceptionHandler} just call
 * <code>install( new StandardDispatchModule( MyExceptionHandler.class )</code>
 * in another module.
 * 
 * @author David Peterson, Eugene Popovich
 */
public class StandardDispatchModule extends AbstractDispatchModule
{

	Class<? extends StandardDispatchServiceAsync> standardDispatchServiceAsyncType;

	/**
	 * Constructs a new GIN configuration module that sets up a
	 * {@link net.customware.http.dispatch.client.DispatchAsync} implementation,
	 * using the
	 * {@link net.customware.http.dispatch.client.DefaultExceptionHandler}.
	 */
	public StandardDispatchModule(
			Class<? extends StandardDispatchServiceAsync> standardDispatchServiceAsyncType)
	{
		this(DefaultExceptionHandler.class, standardDispatchServiceAsyncType);
	}

	public StandardDispatchModule(
			Class<? extends ExceptionHandler> exceptionHandlerType,
			Class<? extends StandardDispatchServiceAsync> standardDispatchServiceAsyncType)
	{
		super(exceptionHandlerType);
		this.standardDispatchServiceAsyncType = standardDispatchServiceAsyncType;
	}

	@Override
	protected void configure()
	{
		super.configure();
		bind(StandardDispatchServiceAsync.class).to(
				standardDispatchServiceAsyncType);
	}

	@Provides
	@Singleton
	protected DispatchAsync provideDispatchAsync(
			ExceptionHandler exceptionHandler,
			StandardDispatchServiceAsync service)
	{
		return new StandardDispatchAsync(exceptionHandler, service);
	}

}
