package com.jutem.log.collector.combiner;

import com.jutem.log.collector.common.LogType;

public class Combiner {
	public static final String SEPARATOR = "<<<"; //日志分隔符

	private StringBuilder logBuilder = new StringBuilder();
	
	public static Combiner newInstance() {
		return new Combiner();
	}

	public Combiner combine(String log) {
		logBuilder.append(log).append(SEPARATOR);
		return this;
	}
	
	public Combiner combine(long log) {
		logBuilder.append(log).append(SEPARATOR);
		return this;
	}

	public Combiner combine(int log) {
		logBuilder.append(log).append(SEPARATOR);
		return this;
	}
	
	public Combiner cbWeb(String app, String host) {
		return combine(app).combine(host);
	}
	
	public Combiner cbReqTime(String method, long time) {
		return combine(LogType.REQTIME).combine(method).combine(time);
	}
	
	public Combiner cbException(String method, String msg, String exception) {
		return combine(LogType.EXCEPTION).combine(method).combine(exception);
	}

	public Combiner cbAvgTime(String method, int allNum, int avgTime) {
		return combine(LogType.AVGTIME).combine(method).combine(allNum).combine(avgTime);
	}
	
	public String toString() {
		return logBuilder.toString();
	}
	
}
