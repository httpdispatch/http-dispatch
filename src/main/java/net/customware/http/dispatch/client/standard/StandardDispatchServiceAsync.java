package net.customware.http.dispatch.client.standard;

import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

public interface StandardDispatchServiceAsync
{

	/**
	 * Executes the specified action.
	 * 
	 * @param action
	 *            The action to execute.
	 * @param callback
	 *            The callback to execute once the action completes.
	 * 
	 * @see net.customware.http.dispatch.server.Dispatch
	 */
	<R extends Result> void execute(Action<R> action, AsyncCallback<R> callback);
}
