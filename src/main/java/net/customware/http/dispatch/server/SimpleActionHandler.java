package net.customware.http.dispatch.server;

import net.customware.http.dispatch.server.standard.GenericUtils;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.shared.Result;

/**
 * 
 * The <tt>SimpleActionHandler</tt> is a base implementation which allows for
 * only the {@link #execute(Action, ExecutionContext)} method to be implemented.
 * 
 * <p>It provides an empty implementation for the
 * {@link #rollback(Action, Result, ExecutionContext)} method infers the action
 * type from the class type parameters.</p>
 * 
 * @author Robert Munteanu
 * 
 * @param <A>
 *            The {@link Action} implementation.
 * @param <R>
 *            The {@link Result} implementation.
 * 
 * @see AbstractActionHandler
 */
public abstract class SimpleActionHandler<A extends Action<R>, R extends Result>
		implements ActionHandler<A, R>
{

	private final Class<A> actionType;

	@SuppressWarnings("unchecked")
	public SimpleActionHandler()
	{

		actionType = (Class<A>) GenericUtils
				.getFirstTypeParameterDeclaredOnSuperclass(getClass());
	}

	@Override
	public Class<A> getActionType()
	{

		return actionType;
	}

	@Override
	public void rollback(A action, R result, ExecutionContext context)
			throws DispatchException
	{

	};
}
