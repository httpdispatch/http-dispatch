package net.customware.http.dispatch.server.guice;

import net.customware.http.dispatch.server.ActionHandler;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

public interface ActionHandlerMap<A extends Action<R>, R extends Result>
{
	public Class<A> getActionClass();

	public Class<? extends ActionHandler<A, R>> getActionHandlerClass();
}
