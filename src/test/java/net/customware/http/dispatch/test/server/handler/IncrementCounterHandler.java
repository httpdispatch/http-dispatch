package net.customware.http.dispatch.test.server.handler;

import net.customware.http.dispatch.server.ActionHandler;
import net.customware.http.dispatch.server.ExecutionContext;
import net.customware.http.dispatch.shared.ActionException;
import net.customware.http.dispatch.test.shared.IncrementCounter;
import net.customware.http.dispatch.test.shared.IncrementCounterResult;

public class IncrementCounterHandler implements
		ActionHandler<IncrementCounter, IncrementCounterResult>
{

	private Counter counter;

	public IncrementCounterHandler(Counter counter)
	{
		this.counter = counter;
	}

	@Override
	public Class<IncrementCounter> getActionType()
	{
		return IncrementCounter.class;
	}

	@Override
	public IncrementCounterResult execute(IncrementCounter action,
			ExecutionContext context) throws ActionException
	{
		counter.setValue(counter.getValue() + action.getAmount());
		return new IncrementCounterResult(action.getAmount(),
				counter.getValue());
	}

	@Override
	public void rollback(IncrementCounter action,
			IncrementCounterResult result, ExecutionContext context)
			throws ActionException
	{
		// Reset to the previous value.
		counter.setValue(result.getCurrent() - result.getAmount());
	}
}