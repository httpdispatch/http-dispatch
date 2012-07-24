package net.customware.http.dispatch.client.secure;

import net.customware.http.dispatch.client.AbstractDispatchAsync;
import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.client.DispatchAsync;
import net.customware.http.dispatch.client.ExceptionHandler;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;
import net.customware.http.dispatch.shared.secure.InvalidSessionException;

/**
 * This class is the default implementation of {@link DispatchAsync}, which is
 * essentially the client-side access to the {@link Dispatch} class on the
 * server-side.
 * 
 * @author David Peterson, Eugene Popovich
 */
public class SecureDispatchAsync extends AbstractDispatchAsync
{

	private final SecureDispatchServiceAsync realService;

	private final SecureSessionAccessor secureSessionAccessor;

	public SecureDispatchAsync(ExceptionHandler exceptionHandler,
			SecureSessionAccessor secureSessionAccessor,
			SecureDispatchServiceAsync realService)
	{
		super(exceptionHandler);
		this.secureSessionAccessor = secureSessionAccessor;
		this.realService = realService;
	}

	@Override
	public <A extends Action<R>, R extends Result> void execute(final A action,
			final AsyncCallback<R> callback)
	{

		String sessionId = secureSessionAccessor.getSessionId(realService
				.getCookieStore());

		realService.execute(sessionId, action, new AsyncCallback<R>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				SecureDispatchAsync.this.onFailure(action, caught, callback);
			}

			@Override
			public void onSuccess(R result)
			{
				SecureDispatchAsync.this.onSuccess(action, result, callback);
			}
		});
	}

	@Override
	protected <A extends Action<R>, R extends Result> void onFailure(A action,
			Throwable caught, final AsyncCallback<R> callback)
	{
		if (caught instanceof InvalidSessionException)
		{
			secureSessionAccessor.clearSessionId(realService.getCookieStore());
		}

		super.onFailure(action, caught, callback);

	}
}
