package net.customware.http.dispatch.server.secure;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementors must provide an implementation of this interface and provide it
 * to the {@link SecureDispatchService} implementation so that it can check for
 * valid session ids.
 * 
 * @author David Peterson, Eugene Popovich
 */
public interface SecureSessionValidator
{
	boolean isValid(String sessionId, HttpServletRequest req);
}
