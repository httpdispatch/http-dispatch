package net.customware.http.dispatch.test.shared;

import net.customware.http.dispatch.shared.Result;

public class ResetCounterResult implements Result
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int oldValue;
	private int newValue;

	@SuppressWarnings("unused")
	private ResetCounterResult()
	{
	}

	public ResetCounterResult(int oldValue, int newValue)
	{
		this.newValue = newValue;
		this.oldValue = oldValue;
	}

	public int getOldValue()
	{
		return oldValue;
	}

	public int getNewValue()
	{
		return newValue;
	}
}
