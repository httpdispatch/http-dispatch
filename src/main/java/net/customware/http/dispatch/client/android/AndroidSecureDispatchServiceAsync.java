package net.customware.http.dispatch.client.android;

import net.customware.http.dispatch.client.AsyncCallback;
import net.customware.http.dispatch.client.secure.SecureDispatchServiceAsync;
import net.customware.http.dispatch.client.util.CustomCookieStore;
import net.customware.http.dispatch.client.util.HttpUtils;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.Result;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Handler;

public class AndroidSecureDispatchServiceAsync implements
		SecureDispatchServiceAsync
{
	private final String dispatchServiceUri;
	private HttpClient httpClient;
	private CustomCookieStore cookieStore;
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public AndroidSecureDispatchServiceAsync(
			String dispatchServiceUri)
	{
		this.dispatchServiceUri = dispatchServiceUri;

		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 100);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
				new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		// This connection manager must be used if more than one thread will
		// be using the HttpClient.
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);
		httpClient = HttpUtils
				.getHttpClient(cm);
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
		// allows non-"edt" thread to be re-inserted into the "edt" queue
		final Handler uiThreadCallback = new Handler();
		new Thread()
		{
			@Override
			public void run()
			{
				final Object result = getResult(action, sessionId);
				// performs rendering in the "edt" thread, after background
				// operation is
				// complete
				final Runnable runInUIThread = new Runnable()
				{
					@Override
					public void run()
					{
						HttpUtils.processResult(result, callback);
					}

				};
				uiThreadCallback.post(runInUIThread);
			}
		}.start();
	}

	@Override
	public CustomCookieStore getCookieStore()
	{
		return cookieStore;
	}
}
