package net.customware.http.dispatch.server.guice;

import net.customware.http.dispatch.client.secure.SecureDispatchService;
import net.customware.http.dispatch.server.Dispatch;
import net.customware.http.dispatch.server.secure.AbstractSecureDispatchServlet;
import net.customware.http.dispatch.server.secure.SecureSessionValidator;
import net.customware.http.dispatch.shared.secure.InvalidSessionException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * A servlet implementation of the {@link SecureDispatchService}. This will
 * call the provided {@link SecureSessionValidator} to confirm that the provided
 * session ID is still valid before executing any actions. If not, an
 * {@link InvalidSessionException} is thrown back to the client.
 * 
 * @author David Peterson, Eugene Popovich
 */
@Singleton
public class GuiceSecureDispatchServlet extends AbstractSecureDispatchServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Dispatch dispatch;

	private final SecureSessionValidator sessionValidator;

	@Inject
	public GuiceSecureDispatchServlet(Dispatch dispatch,
			SecureSessionValidator sessionValidator)
	{
		this.dispatch = dispatch;
		this.sessionValidator = sessionValidator;
	}

	@Override
	public SecureSessionValidator getSessionValidator()
	{
		return sessionValidator;
	}

	@Override
	protected Dispatch getDispatch()
	{
		return dispatch;
	}

}
