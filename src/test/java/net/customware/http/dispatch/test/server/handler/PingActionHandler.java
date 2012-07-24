package net.customware.http.dispatch.test.server.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.customware.http.dispatch.server.ExecutionContext;
import net.customware.http.dispatch.server.SimpleActionHandler;
import net.customware.http.dispatch.shared.ActionException;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.test.shared.PingAction;
import net.customware.http.dispatch.test.shared.PingActionResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class PingActionHandler extends
		SimpleActionHandler<PingAction, PingActionResult>
{
	protected final Logger log = LoggerFactory.getLogger(getClass());
	public static final String FIRST_ACCESS_SESSION_FLAG = "LOGGED_IN";
	@Inject
	protected Provider<HttpServletRequest> servletRequest;

	@Override
	public PingActionResult execute(PingAction action,
			ExecutionContext context) throws DispatchException
	{
		try
		{
			if (servletRequest != null)
			{
				HttpSession session = servletRequest.get().getSession(true);
				Boolean result = (Boolean) session
						.getAttribute(FIRST_ACCESS_SESSION_FLAG);
				if (result == null)
				{
					log.debug("First session access");
					session.setAttribute(FIRST_ACCESS_SESSION_FLAG, true);
				} else
				{
					log.debug("Valid session");
				}
			}
			if (action.isGenerateException())
			{
				throw new Exception("Generated exception");
			} else if (action.isNullResult())
			{
				return null;
			} else
			{
				Object object = action.getObject();
				log.debug("Received object " + object);
				return new PingActionResult(object);
			}
		} catch (Exception cause)
		{
			log.error("Unable to perform ping action", cause);
			throw new ActionException(cause);
		}
	}

	@Override
	public void rollback(PingAction action, PingActionResult result,
			ExecutionContext context) throws DispatchException
	{
		log.debug("PingAction rollback called");
	}

}
