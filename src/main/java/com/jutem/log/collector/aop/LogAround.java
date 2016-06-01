package com.jutem.log.collector.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LogAround {
	boolean reqTime() default false; //监控方法运行时间 监控每一次响应时间
	boolean avgTime() default false; //平均响应时间监控 监控一分钟平均响应时间 TODO 可自定义时间
	boolean exception() default false; //监控方法抛出异常
}
 