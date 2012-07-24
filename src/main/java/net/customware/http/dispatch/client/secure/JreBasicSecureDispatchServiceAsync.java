package net.customware.http.dispatch.client.secure;

import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.client.util.CustomCookieStore;
import net.customware.http.dispatch.client.util.HttpUtils;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JreBasicSecureDispatchServiceAsync implements
		SecureDispatchServiceAsync
{
	private final String dispatchServiceUri;
	private HttpClient httpClient;
	private CustomCookieStore cookieStore;
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public JreBasicSecureDispatchServiceAsync(String dispatchServiceUri)
	{
		this(dispatchServiceUri, new PoolingClientConnectionManager());
	}

	public JreBasicSecureDispatchServiceAsync(String dispatchServiceUri,
			ClientConnectionManager connectionManager)
	{
		this.dispatchServiceUri = dispatchServiceUri;

		httpClient = HttpUtils
				.getHttpClient(connectionManager);
		cookieStore = new CustomCookieStore();
	}

	public Object getResult(Action<?> action, String sesionId)
	{
		return HttpUtils.getResult(dispatchServiceUri, httpClient,
				cookieStore,
				action, new Object[]
				{
					sesionId
				}, log);
	}

	@Override
	public <R extends Result> void execute(final String sessionId,
			final Action<R> action,
			final AsyncCallback<R> callback)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				final Object result = getResult(action, sessionId);
				HttpUtils.processResult(result, callback);
			}
		}.start();
	}

	@Override
	public CustomCookieStore getCookieStore()
	{
		return cookieStore;
	}

}
