package net.customware.http.dispatch.shared.secure;

import net.customware.http.dispatch.shared.Action;

/**
 * A simple username/password authentication request. If successful, a session
 * has been created and a {@link SecureSessionResult} is returned.
 * 
 * @author David Peterson, Eugene Popovich
 */
public class SimpleAuthenticationAction implements Action<SecureSessionResult>
{
	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	SimpleAuthenticationAction()
	{
	}

	public SimpleAuthenticationAction(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}
}
