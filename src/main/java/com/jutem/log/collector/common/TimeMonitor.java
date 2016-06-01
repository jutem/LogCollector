/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.jutem.log.collector.common;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.jutem.log.collector.combiner.Combiner;

/**
 * 参考土豆前辈的代码
 * 统计类
 */
public class TimeMonitor{
	
	//监控map
	private ConcurrentHashMap<String, StatisticsTimeMonitor> monitorMap = new ConcurrentHashMap<String, StatisticsTimeMonitor>();
	
	private static TimeMonitor timeMonitor = new TimeMonitor();
	public static TimeMonitor getInstance() {
		return timeMonitor;
	}
	
	/**
	 * @param cb 日志头
	 */
	public List<String> getMsgs(Combiner cb) {
		if(cb == null) {
			cb = Combiner.newInstance();
		}
		
		List<String> reuslt = new LinkedList<String>();
		for(StatisticsTimeMonitor monitor : monitorMap.values()) {
			String log = cb.cbAvgTime(monitor.getName(), monitor.getAllNums().intValue(), monitor.getAverageExeTime()).toString();
			monitor.clear();
			reuslt.add(log);
		}
		return reuslt;
	}

	public void exeNormal(String methodName, long execTime) {
		if(monitorMap.containsKey(methodName)){
			monitorMap.get(methodName).exeNormal(execTime);
		} else {
			monitorMap.putIfAbsent(methodName, new StatisticsTimeMonitor(methodName, execTime));
		}
	}
	
	/**
	 * 统计bean
	 */
	private class StatisticsTimeMonitor {
		
		private String name;
		private AtomicInteger allNums = new AtomicInteger(0);
		private AtomicLong exeTimes = new AtomicLong(0L);
		
		public StatisticsTimeMonitor(String paramString, long paramLong) {
			this.name = paramString;
			this.exeTimes.set(paramLong);
			this.allNums.incrementAndGet();
		}

		public void exeNormal(long paramLong) {
			this.allNums.incrementAndGet();
			this.exeTimes.addAndGet(paramLong);
		}

		public void clear() {
			this.allNums.set(0);
			this.exeTimes.set(0L);
		}

		public int getAverageExeTime() {
			return exeTimes.intValue() / allNums.intValue();
		}

		public String getName() {
			return name;
		}

		public AtomicInteger getAllNums() {
			return allNums;
		}
		
	}
}