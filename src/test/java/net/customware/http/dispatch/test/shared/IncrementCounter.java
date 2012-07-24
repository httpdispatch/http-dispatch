package net.customware.http.dispatch.test.shared;

import net.customware.http.dispatch.shared.Action;

public class IncrementCounter implements Action<IncrementCounterResult>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int amount;

	/** For serialization only. */
	IncrementCounter()
	{
	}

	public IncrementCounter(int amount)
	{
		this.amount = amount;
	}

	public int getAmount()
	{
		return amount;
	}
}