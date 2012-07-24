package net.customware.http.dispatch.client.secure;

import net.customware.http.dispatch.client.util.CustomCookieStore;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.HttpContext;

public class CookieSecureSessionAccessor implements SecureSessionAccessor
{

	private String cookieName;

	public CookieSecureSessionAccessor(String cookieName)
	{
		this.cookieName = cookieName;
	}

	@Override
	public boolean clearSessionId(CustomCookieStore cookieStore)
	{
		// CustomCookieStore cookieStore = getCookieStore(context);
		if (cookieStore != null)
		{
			return cookieStore.removeCookie(cookieName);
		}
		return false;
	}

	@Override
	public String getSessionId(CustomCookieStore cookieStore)
	{
		// CustomCookieStore cookieStore = getCookieStore(context);
		if (cookieStore != null)
		{
			return cookieStore.getCookie(cookieName);
		}
		return null;
	}

	public CustomCookieStore getCookieStore(HttpContext context)
	{
		Object object = context.getAttribute(ClientContext.COOKIE_STORE);
		CustomCookieStore result = null;
		if (object != null && object instanceof CustomCookieStore)
		{
			result = (CustomCookieStore) object;
		}
		return result;
	}

}