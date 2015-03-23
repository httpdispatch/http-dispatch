# Introduction #

This is a simple example of how to create your own Action/Result/Handler. The source code for the examples is available in the [download](http://code.google.com/p/http-dispatch/downloads/list) section


# Details #

First create simple result wrapper around some object.

```java

public class PingActionResult extends AbstractSimpleResult<Object>
{
private static final long serialVersionUID = 1L;


public PingActionResult(Object object)
{
super(object);
}
}
```

Then create simple ping action command

```java

public class PingAction implements Action<PingActionResult>
{
private static final long serialVersionUID = 1L;

private Object object;

//generate server exception flag
private boolean generateException;

//return null result flag
private boolean nullResult;

public PingAction(Object object)
{
this.object = object;
}

public PingAction(boolean nullResult, boolean generateException)
{
this.generateException = generateException;
this.nullResult = nullResult;
}


public Object getObject()
{
return object;
}

public void setObject(Object object)
{
this.object = object;
}

public boolean isGenerateException()
{
return generateException;
}

public void setGenerateException(boolean generateException)
{
this.generateException = generateException;
}

public boolean isNullResult()
{
return nullResult;
}

public void setNullResult(boolean nullResult)
{
this.nullResult = nullResult;
}

}
```

On the server side we need to create PingAction handler

```java

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

```

We use FIRST\_ACCESS\_SESSION\_FLAG session flag to demonstrate how the security servlet works

Then configure GuiceServletModule

```java

public class DispatchServletModule extends ServletModule
{

@Override
public void configureServlets()
{
// NOTE: the servlet context will probably need changing
serve("/guice_secure_dispatch").with(GuiceSecureDispatchServlet.class);
}

}
```

Based on PingActionHandler FIRST\_ACCESS\_SESSION\_FLAG session flag secure session validator. Originally you should use currently logged in user information instead of that flag.

```java

public class PingActionJBossSecureSessionValidator implements
SecureSessionValidator
{
@Override
public boolean isValid(String clientSessionId, HttpServletRequest req)
{

Object sessionFlag = req.getSession(true).getAttribute(
PingActionHandler.FIRST_ACCESS_SESSION_FLAG);
if (sessionFlag != null)
{
// User is logged in, now try to match session tokens
// to prevent CSRF
String sessionId = "";
Cookie[] cookies = req.getCookies();
for (Cookie cookie : cookies)
{
if (cookie.getName().equals("JSESSIONID"))
{
sessionId = cookie.getValue();
break;
}
}
return sessionId.equals(clientSessionId);
}
return true;
}
}
```

Let's write our server module configuration
```java

/**
* Module which binds the handlers and configurations
*
*/
public class ServerModule extends ActionHandlerModule
{

@Override
protected void configureHandlers()
{
bind(SecureSessionValidator.class)
.to(PingActionJBossSecureSessionValidator.class);

bindHandler(PingAction.class, PingActionHandler.class);
bindHandler(BatchAction.class, BatchActionHandler.class);
}

}
```

Guice servlet config
```java

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
```

web.xml server configuration
```xml

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" version="2.5">
<filter>
<filter-name>guiceFilter

Unknown end tag for &lt;/filter-name&gt;


<filter-class>com.google.inject.servlet.GuiceFilter

Unknown end tag for &lt;/filter-class&gt;




Unknown end tag for &lt;/filter&gt;


<filter-mapping>
<filter-name>guiceFilter

Unknown end tag for &lt;/filter-name&gt;


<url-pattern>/*

Unknown end tag for &lt;/url-pattern&gt;




Unknown end tag for &lt;/filter-mapping&gt;


<listener>
<listener-class>net.customware.http.dispatch.test.server.guice.TestGuiceServletConfig

Unknown end tag for &lt;/listener-class&gt;




Unknown end tag for &lt;/listener&gt;




Unknown end tag for &lt;/web-app&gt;


```

On the client side you need to create cookie secure session accessor for security validation
```java

public class JBossCookieSecureSessionAccessor extends
CookieSecureSessionAccessor
{
public JBossCookieSecureSessionAccessor()
{
super("JSESSIONID");
}

}
```
Create custom secure dispatch service async
```java

public class CustomSecureDispatchServiceAsync extends
JreBasicSecureDispatchServiceAsync
{
public CustomSecureDispatchServiceAsync()
{
super(DISPATCH_URL);
}
}
```
Note that for Android platform you should to extend AndroidSecureDispatchServiceAsync class.

Create dispatch async with guice
```java

Injector injector = Guice.createInjector(
new SecureDispatchModule(
CustomSecureDispatchServiceAsync.class,
JBossCookieSecureSessionAccessor.class)
);
DispatchAsync dispatch = injector.getInstance(DispatchAsync.class);
```

Let's send and receive basic string object
```java

final String testObject = "Test String Object";
PingAction pingRequest = new PingAction(testObject);
dispatch.execute(pingRequest, new AsyncCallback<PingActionResult>()
{
@Override
public void onSuccess(PingActionResult result)
{
System.out.println("Received "+result.get()+" object from server");
}
@Override
public void onFailure(Throwable e)
{
e.printStackTrace();
}
});
```