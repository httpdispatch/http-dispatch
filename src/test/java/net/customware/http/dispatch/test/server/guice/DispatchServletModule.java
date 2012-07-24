package net.customware.http.dispatch.test.server.guice;

import net.customware.http.dispatch.server.guice.GuiceSecureDispatchServlet;

import com.google.inject.servlet.ServletModule;

public class DispatchServletModule extends ServletModule
{

	@Override
	public void configureServlets()
	{
		// NOTE: the servlet context will probably need changing
		serve("/guice_secure_dispatch").with(GuiceSecureDispatchServlet.class);
	}

}