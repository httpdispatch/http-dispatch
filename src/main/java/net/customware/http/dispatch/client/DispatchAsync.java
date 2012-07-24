package net.customware.http.dispatch.client;

import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

/**
 * This is an asynchronous equivalent of the {@link Dispatch} interface on the
 * server side. The reason it exists is because GWT currently can't correctly
 * handle having generic method templates in method signatures (eg.
 * <code>&lt;A&gt; A
 * create( Class<A> type )</code>)
 * 
 * @author David Peterson, Eugene Popovich
 */
public interface DispatchAsync
{
	<A extends Action<R>, R extends Result> void execute(A action,
			AsyncCallback<R> callback);
}
