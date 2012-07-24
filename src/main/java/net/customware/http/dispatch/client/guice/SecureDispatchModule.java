package net.customware.http.dispatch.client.guice;

import net.customware.http.dispatch.client.DefaultExceptionHandler;
import net.customware.http.dispatch.client.DispatchAsync;
import net.customware.http.dispatch.client.ExceptionHandler;
import net.customware.http.dispatch.client.secure.CookieSecureSessionAccessor;
import net.customware.http.dispatch.client.secure.SecureDispatchAsync;
import net.customware.http.dispatch.client.secure.SecureDispatchServiceAsync;
import net.customware.http.dispatch.client.secure.SecureSessionAccessor;

import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * This module binds the {@link DispatchAsync} to {@link SecureDispatchAsync}.
 * For simple cases, just set this as a \@GinModule in your {@link Ginjector}
 * instance.
 * <p/>
 * If you want to provide a custom {@link ExceptionHandler} just call
 * <code>install( new StandardDispatchModule( MyExceptionHandler.class )</code>
 * in another module.
 * <p/>
 * You must also provide another module which binds an implementation of
 * {@link SecureSessionAccessor}, such as {@link CookieSecureSessionAccessor} or
 * {@link AppEngineSecureSessionAccessor}.
 * 
 * @author David Peterson, Eugene Popovich
 */
public class SecureDispatchModule extends AbstractDispatchModule
{

	Class<? extends SecureDispatchServiceAsync> secureDispatchServiceAsyncType;
	Class<? extends SecureSessionAccessor> secureSessionAccessorType;

	/**
	 * Constructs a new GIN configuration module that sets up a secure
	 * {@link net.customware.http.dispatch.client.DispatchAsync} implementation,
	 * using the
	 * {@link net.customware.http.dispatch.client.DefaultExceptionHandler}.
	 */
	public SecureDispatchModule(
			Class<? extends SecureDispatchServiceAsync> secureDispatchServiceAsyncType,
			Class<? extends SecureSessionAccessor> secureSessionAccessorType)
	{
		this(DefaultExceptionHandler.class, secureDispatchServiceAsyncType,
				secureSessionAccessorType);
	}

	/**
	 * Constructs a new GIN configuration module that sets up a secure
	 * {@link net.customware.http.dispatch.client.DispatchAsync} implementation,
	 * using the provided {@link ExceptionHandler} implementation class.
	 * 
	 * @param exceptionHandlerType
	 *            The {@link ExceptionHandler} implementation class.
	 */
	public SecureDispatchModule(
			Class<? extends ExceptionHandler> exceptionHandlerType,
			Class<? extends SecureDispatchServiceAsync> secureDispatchServiceAsyncType,
			Class<? extends SecureSessionAccessor> secureSessionAccessorType)
	{
		super(exceptionHandlerType);
		this.secureDispatchServiceAsyncType = secureDispatchServiceAsyncType;
		this.secureSessionAccessorType = secureSessionAccessorType;
	}

	@Override
	protected void configure()
	{
		super.configure();
		bind(SecureDispatchServiceAsync.class).to(
				secureDispatchServiceAsyncType);
		bind(SecureSessionAccessor.class).to(secureSessionAccessorType);
	}

	@Provides
	@Singleton
	protected DispatchAsync provideDispatchAsync(
			ExceptionHandler exceptionHandler,
			SecureSessionAccessor secureSessionAccessor,
			SecureDispatchServiceAsync service)
	{
		return new SecureDispatchAsync(exceptionHandler, secureSessionAccessor,
				service);
	}

}
