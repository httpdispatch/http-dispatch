package net.customware.http.dispatch.server.guice;

import net.customware.http.dispatch.server.ActionHandlerRegistry;
import net.customware.http.dispatch.server.SimpleDispatch;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * A simple extension of {@link SimpleDispatch} with Guice annotations.
 * 
 * @author David Peterson, Eugene Popovich
 */
@Singleton
public class GuiceDispatch extends SimpleDispatch
{

	@Inject
	public GuiceDispatch(ActionHandlerRegistry handlerRegistry)
	{
		super(handlerRegistry);
	}
}
