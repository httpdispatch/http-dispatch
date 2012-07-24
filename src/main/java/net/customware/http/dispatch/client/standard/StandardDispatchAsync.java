package net.customware.http.dispatch.client.standard;

import net.customware.http.dispatch.client.AbstractDispatchAsync;
import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.client.DispatchAsync;
import net.customware.http.dispatch.client.ExceptionHandler;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

/**
 * This class is the default implementation of {@link DispatchAsync}, which is
 * essentially the client-side access to the {@link Dispatch} class on the
 * server-side.
 * 
 * @author David Peterson, Eugene Popovich
 */
public class StandardDispatchAsync extends AbstractDispatchAsync
{

	private final StandardDispatchServiceAsync realService;

	public StandardDispatchAsync(ExceptionHandler exceptionHandler,
			StandardDispatchServiceAsync service)
	{
		super(exceptionHandler);
		this.realService = service;
	}

	@Override
	public <A extends Action<R>, R extends Result> void execute(final A action,
			final AsyncCallback<R> callback)
	{
		realService.execute(action, new AsyncCallback<R>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				StandardDispatchAsync.this.onFailure(action, caught, callback);
			}

			@Override
			public void onSuccess(R result)
			{
				StandardDispatchAsync.this.onSuccess(action, result, callback);
			}
		});
	}

}
