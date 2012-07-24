package net.customware.http.dispatch.shared;

/**
 * These are thrown by {@link Dispatch#execute(Action)} if there is a
 * problem executing a particular {@link Action}.
 * 
 * @author David Peterson, Eugene Popovich
 */
public class ActionException extends DispatchException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ActionException()
	{
	}

	public ActionException(String message)
	{
		super(message);
	}

	public ActionException(Throwable cause)
	{
		super(cause);
	}

	public ActionException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
