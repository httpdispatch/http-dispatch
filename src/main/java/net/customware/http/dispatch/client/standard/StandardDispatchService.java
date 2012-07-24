package net.customware.http.dispatch.client.standard;

import net.customware.http.dispatch.client.DispatchAsync;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.shared.Result;

/**
 * There currently seem to be GWT compilation problems with services that use
 * generic templates in method parameters. As such, they are stripped here, but
 * added back into the {@link Dispatch} and {@link DispatchAsync}, which are
 * the interfaces that should be accessed by regular code.
 * <p/>
 * Once GWT can compile templatized methods correctly, this should be merged
 * into a single pair of interfaces.
 * 
 * @author David Peterson, Eugene Popovich
 */
public interface StandardDispatchService
{
	<R extends Result> R execute(Action<R> action) throws DispatchException;
}
