package net.customware.http.dispatch.client.secure;

import net.customware.http.dispatch.client.util.CustomCookieStore;

/**
 * Provides access to the session ID.
 * 
 * @author David Peterson, Eugene Popovich
 */
public interface SecureSessionAccessor
{
	/**
	 * Gets the current session ID.
	 * 
	 * @return The ID.
	 */
	String getSessionId(CustomCookieStore cookieStore);

	/**
	 * Clears the session id, effectively closing the current session.
	 * 
	 * @return <code>true</code> if successful.
	 */
	boolean clearSessionId(CustomCookieStore cookieStore);
}
