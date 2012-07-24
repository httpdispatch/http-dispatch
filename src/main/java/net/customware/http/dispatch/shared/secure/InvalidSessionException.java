package net.customware.http.dispatch.shared.secure;

import net.customware.http.dispatch.shared.ServiceException;

public class InvalidSessionException extends ServiceException
{

	private static final long serialVersionUID = 1L;

	public InvalidSessionException()
	{
		super();
	}

	public InvalidSessionException(String message)
	{
		super(message);
	}
}
