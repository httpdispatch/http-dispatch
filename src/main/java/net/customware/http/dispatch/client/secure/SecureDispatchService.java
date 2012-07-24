package net.customware.http.dispatch.client.secure;

import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.shared.Result;

public interface SecureDispatchService
{
	Result execute(String sessionId, Action<?> action) throws DispatchException;
}
