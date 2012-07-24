package net.customware.http.dispatch.server.secure;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.servlet.http.HttpServletRequest;

import net.customware.http.dispatch.server.Dispatch;
import net.customware.http.dispatch.server.standard.AbstractStandardDispatchServlet;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.shared.Result;
import net.customware.http.dispatch.shared.ServiceException;
import net.customware.http.dispatch.shared.secure.InvalidSessionException;

public abstract class AbstractSecureDispatchServlet extends
		AbstractStandardDispatchServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected Object getResult(ObjectInputStream ois,
			HttpServletRequest request) throws IOException,
			ClassNotFoundException, DispatchException
	{
		Object result;
		Action<? extends Result> input = (Action<? extends Result>) ois
				.readObject();
		String sessionId = (String) ois.readObject();
		log.debug("got the action from the client:" + input);

		result = execute(sessionId, input, request);
		return result;
	}

	private Result execute(String sessionId, Action<?> action,
			HttpServletRequest request)
			throws DispatchException
	{
		try
		{

			SecureSessionValidator sessionValidator = getSessionValidator();
			if (sessionValidator == null)
				throw new ServiceException(
						"No session validator found for servlet '"
								+ getServletName()
								+ "' . Please verify your server-side configuration.");

			Dispatch dispatch = getDispatch();
			if (dispatch == null)
				throw new ServiceException("No dispatch found for servlet '"
						+ getServletName()
						+ "' . Please verify your server-side configuration.");

			if (sessionValidator.isValid(sessionId, request))
			{
				return dispatch.execute(action);
			} else
			{
				throw new InvalidSessionException();
			}
		} catch (RuntimeException e)
		{
			log("Exception while executing " + action.getClass().getName()
					+ ": " + e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	protected abstract SecureSessionValidator getSessionValidator();
}