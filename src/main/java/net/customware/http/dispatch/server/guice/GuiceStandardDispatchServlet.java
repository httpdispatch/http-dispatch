package net.customware.http.dispatch.server.guice;

import net.customware.http.dispatch.server.Dispatch;
import net.customware.http.dispatch.server.standard.AbstractStandardDispatchServlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GuiceStandardDispatchServlet extends
		AbstractStandardDispatchServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Dispatch dispatch;

	@Inject
	public GuiceStandardDispatchServlet(Dispatch dispatch)
	{
		this.dispatch = dispatch;
	}

	@Override
	protected Dispatch getDispatch()
	{
		return dispatch;
	}
}
