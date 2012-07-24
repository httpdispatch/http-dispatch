package net.customware.http.dispatch.client.standard;

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

public class JreBasicStandardDispatchServiceAsync implements
		StandardDispatchServiceAsync
{
	private final String dispatchServiceUri;
	private HttpClient httpClient;
	private CustomCookieStore cookieStore;

	public JreBasicStandardDispatchServiceAsync(String dispatchServiceUri)
	{
		this(dispatchServiceUri, new PoolingClientConnectionManager());
	}

	public JreBasicStandardDispatchServiceAsync(String dispatchServiceUri,
			ClientConnectionManager connectionManager)
	{
		this.dispatchServiceUri = dispatchServiceUri;

		httpClient = HttpUtils
				.getHttpClient(connectionManager);
		cookieStore = new CustomCookieStore();
	}

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public <R extends Result> void execute(
			final Action<R> action,
			final AsyncCallback<R> callback)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				final Object result = getResult(action);
				HttpUtils.processResult(result, callback);
			}
		}.start();
	}

	public Object getResult(Action<?> action)
	{
		return HttpUtils.getResult(dispatchServiceUri, httpClient,
				cookieStore,
				action, log);
	}
}
