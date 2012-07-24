package net.customware.http.dispatch.test.server.handler;

public class Counter
{
	private int value = 0;

	public synchronized int getValue()
	{
		return value;
	}

	public synchronized void setValue(int value)
	{
		this.value = value;
	}
}
