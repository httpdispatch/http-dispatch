package net.customware.http.dispatch.test.server.secure;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.customware.http.dispatch.server.secure.SecureSessionValidator;
import net.customware.http.dispatch.test.server.handler.PingActionHandler;

public class PingActionJBossSecureSessionValidator implements
		SecureSessionValidator
{
	@Override
	public boolean isValid(String clientSessionId, HttpServletRequest req)
	{

		Object sessionFlag = req.getSession(true).getAttribute(
				PingActionHandler.FIRST_ACCESS_SESSION_FLAG);
		if (sessionFlag != null)
		{
			// User is logged in, now try to match session tokens
			// to prevent CSRF
			String sessionId = "";
			Cookie[] cookies = req.getCookies();
			for (Cookie cookie : cookies)
			{
				if (cookie.getName().equals("JSESSIONID"))
				{
					sessionId = cookie.getValue();
					break;
				}
			}
			return sessionId.equals(clientSessionId);
		}
		return true;
	}
}
