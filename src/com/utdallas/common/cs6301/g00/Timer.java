package cs6301.g00;

public class Timer
{
	private long startTime, elapsedTime, memAvailable, memUsed;

	public Timer()
	{
		startTime = System.currentTimeMillis();
	}

	public void start()
	{
		startTime = System.currentTimeMillis();
	}

	public Timer end()
	{
		long endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		memAvailable = Runtime.getRuntime().totalMemory();
		memUsed = memAvailable - Runtime.getRuntime().freeMemory();
		return this;
	}

	public String toString()
	{
		return "Time: " + elapsedTime + " msec.\n" + "Memory: " + ( memUsed / 1048576 ) + " MB / " + ( memAvailable / 1048576 ) + " MB.";
	}

}