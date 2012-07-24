package net.customware.http.dispatch.server;

import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

/**
 * Simple abstract super-class for {@link ActionHandler} implementations that
 * forces the {@link Action} class to be passed in as a constructor to the
 * handler. It's arguable if this is any simpler than just implementing the
 * {@link ActionHandler} and its {@link #getActionType()} directly.
 * 
 * @author David Peterson, Eugene Popovich
 * 
 * @param <A>
 *            The {@link Action} implementation.
 * @param <R>
 *            The {@link Result} implementation.
 * 
 * @see SimpleActionHandler
 */
public abstract class AbstractActionHandler<A extends Action<R>, R extends Result>
		implements ActionHandler<A, R>
{

	private final Class<A> actionType;

	public AbstractActionHandler(Class<A> actionType)
	{
		this.actionType = actionType;
	}

	@Override
	public Class<A> getActionType()
	{
		return actionType;
	}

}
