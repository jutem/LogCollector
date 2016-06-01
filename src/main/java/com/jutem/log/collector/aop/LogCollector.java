package com.jutem.log.collector.aop;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.jutem.log.collector.combiner.Combiner;
import com.jutem.log.collector.common.TimeMonitor;
import com.jutem.log.collector.pusher.Pusher;

@Aspect
public class LogCollector {

	private static final Logger logger = Logger.getLogger(LogCollector.class);
	private Pusher pusher;
	private String app; //app名
	private String host; //host地址
	private int intervalsTime; //定时器的间隔时间,单位为秒
	private TimeMonitor timeMonitor; //本地时间处理监控
	
	public LogCollector(String app, String host, Pusher pusher) {
		this(app, host, 60, pusher);
	}

	/**
	 * @param app 应用名
	 * @param host host地址
	 * @param intervalsTime 启动定时器的间隔时间
	 */
	public LogCollector(final String app, final String host, int intervalsTime, final Pusher pusher) {
		super();
		logger.info("<<<<<<< logCollector init start");
		this.app = app;
		this.host = host;
		this.intervalsTime = intervalsTime;
		this.pusher = pusher;
		if(intervalsTime > 0) {
			this.timeMonitor = TimeMonitor.getInstance();
			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {
					Combiner cb = Combiner.newInstance().cbWeb(app, host);
					List<String> logs = timeMonitor.getMsgs(cb);
					for(String log : logs)
						pusher.push(log);
				}
			}, 10, intervalsTime, TimeUnit.SECONDS);
		}
	}
	
	/**
	 * 主around方法，环绕annotation
	 */
	@Around("@annotation(com.jutem.log.collector.aop.LogAround)")
	public Object around(ProceedingJoinPoint point) throws Throwable {

		if (point == null) {
			logger.error("error:########## aop point is nulll, please check");
			return null;
		}
		
		// 调用目标对象的方法并获取返回值
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		LogAround la = method.getAnnotation(LogAround.class);
		
		if(la != null) {

			long startTime = System.currentTimeMillis(); 
			Object targetMethodReturnValue = point.proceed(point.getArgs());
			long endTime = System.currentTimeMillis();
			
			if(la.reqTime())
				executeTimeMonitor(startTime, endTime, method.getName());
			if(la.avgTime())
				executeAvgTimeMonitor(startTime, endTime , method.getName());
			
			return targetMethodReturnValue;
		}
		
		return null;
	}
	
	/**
	 * 时间监控处理
	 * 处理每一次响应时间
	 */
	private void executeTimeMonitor(long startTime, long endTime, String methodName) throws Throwable {
		String log = Combiner.newInstance().cbWeb(app, host).cbReqTime(methodName, endTime - startTime).toString();
		pusher.push(log);
	}
	
	/**
	 * 处理平均响应时间
	 */
	private void executeAvgTimeMonitor(long startTime, long endTime, String methodName) {
		if(intervalsTime <= 0) {
			logger.error("<<<<<<< please set intervalsTime > 0");
		}
		timeMonitor.exeNormal(methodName, endTime - startTime);
	}
	
	/**
	 * exception处理
	 */
//	@AfterThrowing(value = "execution(com.jutem.log.collector.aop.LogAround)", throwing = "e")
//    public void exceptionIntercept(JoinPoint point, Throwable e) {
//       if (point == null) {
//           logger.error("error:method:exceptionIntercept, the point is null here, please check ! ");
//           return;
//       }
// 
//       Method method = ((MethodSignature) point.getSignature()).getMethod();
//       LogAround la = method.getAnnotation(LogAround.class);
//       
//       //TODO 补全参数
//       if(la.excetpionMonitor()) {
//    	   String log = Combiner.newInstance().cbWeb("test", "127.0.0.1").cbException(method.getName(), e.getMessage(), "test exception").toString();
//    	   pusher.push(log);
//       }
//       
//       return;
//    }
	
	
}