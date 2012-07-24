package net.customware.http.dispatch.test.shared;

import net.customware.http.dispatch.shared.Action;

public class ResetCounter implements Action<ResetCounterResult>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int value = 0;

	public ResetCounter()
	{
	}

	public ResetCounter(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
