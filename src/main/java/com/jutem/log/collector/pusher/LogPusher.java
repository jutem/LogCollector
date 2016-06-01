package com.jutem.log.collector.pusher;

import org.apache.log4j.Logger;

/**
 * 把log推到本地log4j日志 
 * @author jutem 
 */
public class LogPusher implements Pusher{
	
	private static Logger logger; 
	
	public LogPusher() {
		logger = Logger.getLogger(LogPusher.class);
	}
	
	public LogPusher(String logName) {
		logger = Logger.getLogger(logName);
	}

	@Override
	public void push(final String log) {
		logger.info(log);
	}
	
}
