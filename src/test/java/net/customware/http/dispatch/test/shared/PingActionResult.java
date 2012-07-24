package net.customware.http.dispatch.test.shared;

import net.customware.http.dispatch.shared.AbstractSimpleResult;

public class PingActionResult extends AbstractSimpleResult<Object>
{
	private static final long serialVersionUID = 1L;

	public PingActionResult(Object object)
	{
		super(object);
	}
}
