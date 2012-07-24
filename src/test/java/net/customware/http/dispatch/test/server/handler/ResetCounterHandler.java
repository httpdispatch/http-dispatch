package net.customware.http.dispatch.test.server.handler;

import net.customware.http.dispatch.server.ActionHandler;
import net.customware.http.dispatch.server.ExecutionContext;
import net.customware.http.dispatch.shared.ActionException;
import net.customware.http.dispatch.test.shared.ResetCounter;
import net.customware.http.dispatch.test.shared.ResetCounterResult;

public class ResetCounterHandler implements
		ActionHandler<ResetCounter, ResetCounterResult>
{

	private final Counter counter;

	public ResetCounterHandler(Counter counter)
	{
		this.counter = counter;
	}

	@Override
	public Class<ResetCounter> getActionType()
	{
		return ResetCounter.class;
	}

	@Override
	public ResetCounterResult execute(ResetCounter action,
			ExecutionContext context) throws ActionException
	{
		int oldValue = counter.getValue();
		counter.setValue(action.getValue());
		return new ResetCounterResult(oldValue, counter.getValue());
	}

	@Override
	public void rollback(ResetCounter action, ResetCounterResult result,
			ExecutionContext context)
			throws ActionException
	{
		counter.setValue(result.getOldValue());
	}

}
