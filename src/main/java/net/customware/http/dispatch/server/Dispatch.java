package net.customware.http.dispatch.server;

import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.shared.Result;

/**
 * Executes actions and returns the results.
 * 
 * @author David Peterson, Eugene Popovich
 */
public interface Dispatch
{

	/**
	 * Executes the specified action and returns the appropriate result.
	 * 
	 * @param <T>
	 *            The {@link Result} type.
	 * @param action
	 *            The {@link Action}.
	 * @return The action's result.
	 * @throws DispatchException
	 *             if the action execution failed.
	 */
	<A extends Action<R>, R extends Result> R execute(A action)
			throws DispatchException;
}
