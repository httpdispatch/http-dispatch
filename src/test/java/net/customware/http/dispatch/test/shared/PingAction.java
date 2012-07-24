package net.customware.http.dispatch.test.shared;

import net.customware.http.dispatch.shared.Action;

public class PingAction implements Action<PingActionResult>
{
	private static final long serialVersionUID = 1L;

	private Object object;
	private boolean generateException;
	private boolean nullResult;

	public boolean isNullResult()
	{
		return nullResult;
	}

	public void setNullResult(boolean nullResult)
	{
		this.nullResult = nullResult;
	}

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
}
