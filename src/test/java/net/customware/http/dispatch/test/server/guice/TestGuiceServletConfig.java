package net.customware.http.dispatch.test.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class TestGuiceServletConfig extends GuiceServletContextListener
{

	@Override
	protected Injector getInjector()
	{
		Injector injector = Guice.createInjector(new ServerModule(),
				new DispatchServletModule());
		return injector;
	}
}