package net.customware.http.dispatch.server;

import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

/**
 * Instances of this interface allow {@link ActionHandler} classes to be
 * registered for specific {@link Action} types. This is typcially to allow
 * lazy-loading of handlers.
 * 
 * @author David Peterson, Eugene Popovich
 * @see LazyActionHandlerRegistry
 * 
 */
public interface ClassActionHandlerRegistry extends ActionHandlerRegistry
{
	/**
	 * Registers the specified {@link ActionHandler} class with the registry.
	 * The class will only
	 * 
	 * @param actionClass
	 *            The action class the handler handles.
	 * @param handlerClass
	 *            The handler class.
	 */
	public <A extends Action<R>, R extends Result> void addHandlerClass(
			Class<A> actionClass,
			Class<? extends ActionHandler<A, R>> handlerClass);

	/**
	 * Removes any registration of the specified class, as well as any instances
	 * which have been created.
	 * 
	 * @param handlerClass
	 */
	public <A extends Action<R>, R extends Result> void removeHandlerClass(
			Class<A> actionClass,
			Class<? extends ActionHandler<A, R>> handlerClass);
}
