package net.customware.http.dispatch.client.secure;

import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.client.util.CustomCookieStore;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

public interface SecureDispatchServiceAsync
{
	<R extends Result> void execute(String sessionId, Action<R> action,
			AsyncCallback<R> callback);

	CustomCookieStore getCookieStore();
}
