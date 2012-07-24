package net.customware.http.dispatch.client;

/**
 * A default implementation of {@link ExceptionHandler} which does not
 * do anything.
 * 
 * @author David Peterson, Eugene Popovich
 */
public class DefaultExceptionHandler implements ExceptionHandler
{

	public DefaultExceptionHandler()
	{
	}

	/**
	 * Always returns {@link Status#CONTINUE}.
	 * 
	 * @param e
	 *            The exception.
	 * @return The status.
	 */
	@Override
	public Status onFailure(Throwable e)
	{
		return Status.CONTINUE;
	}
}
