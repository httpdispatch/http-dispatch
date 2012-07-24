package net.customware.http.dispatch.test.server.handler;

import net.customware.http.dispatch.server.ExecutionContext;
import net.customware.http.dispatch.server.SimpleActionHandler;
import net.customware.http.dispatch.shared.ActionException;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.test.shared.CauseError;
import net.customware.http.dispatch.test.shared.CauseErrorResult;

public class CauseErrorHandler extends
		SimpleActionHandler<CauseError, CauseErrorResult>
{

	@Override
	public CauseErrorResult execute(CauseError action, ExecutionContext context)
			throws DispatchException
	{

		throw new ActionException("Failed as expected.");
	}
}
