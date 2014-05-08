package me.seanmaltby.lonearcher.core.utils;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeRecorder
{
	private static Map<String, List<Long>> timeRecords = new HashMap<String, List<Long>>();
	private static Map<String, Long> startRecords = new HashMap<String, Long>();

	public static void start(String id)
	{
		startRecords.put(id, TimeUtils.nanoTime());
	}

	public static void stop(String id)
	{
		long time = TimeUtils.nanoTime() - startRecords.get(id);
		if(!timeRecords.containsKey(id))
			timeRecords.put(id, new ArrayList<Long>());
		timeRecords.get(id).add(time);
	}

	public static void printInfo()
	{
		for(Map.Entry<String, List<Long>> entry : timeRecords.entrySet())
		{
			double averageTime = average(entry.getValue());
			double averageTimeMilli = averageTime * 0.000001;
			System.out.println(entry.getKey() + " average time (milliseconds) - "+averageTimeMilli);
		}
	}

	private static double average(List<Long> times)
	{
		long sum = 0;
		for(long time : times)
			sum += time;
		return (double) sum / times.size();
	}
}
