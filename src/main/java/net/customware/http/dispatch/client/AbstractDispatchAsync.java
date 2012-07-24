package net.customware.http.dispatch.client;

import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

/**
 * An abstract base class that provides methods that can be called to handle
 * success or failure
 * results from the remote service. These should be called by the implementation
 * of
 * {@link #execute(net.customware.http.dispatch.shared.Action, net.customware.http.dispatch.client.AsyncCallback)}
 * .
 * 
 * @author David Peterson, Eugene Popovich
 */
public abstract class AbstractDispatchAsync implements DispatchAsync
{

	private final ExceptionHandler exceptionHandler;

	public AbstractDispatchAsync(ExceptionHandler exceptionHandler)
	{
		this.exceptionHandler = exceptionHandler;
	}

	protected <A extends Action<R>, R extends Result> void onFailure(A action,
			Throwable caught, final AsyncCallback<R> callback)
	{
		if (exceptionHandler != null
				&& exceptionHandler.onFailure(caught) == ExceptionHandler.Status.STOP)
		{
			return;
		}

		callback.onFailure(caught);
	}

	protected <A extends Action<R>, R extends Result> void onSuccess(A action,
			R result, final AsyncCallback<R> callback)
	{
		callback.onSuccess(result);
	}

}
