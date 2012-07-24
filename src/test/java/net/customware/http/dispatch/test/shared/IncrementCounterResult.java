package net.customware.http.dispatch.test.shared;

import net.customware.http.dispatch.shared.Result;

public class IncrementCounterResult implements Result
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int amount;
	private int current;

	/** For serialization only. */
	IncrementCounterResult()
	{
	}

	public IncrementCounterResult(int amount, int current)
	{
		this.amount = amount;
		this.current = current;
	}

	public int getAmount()
	{
		return amount;
	}

	public int getCurrent()
	{
		return current;
	}
}