# LogCollector
通过注释收集日志到指定地址

0.0.1-snapshot
入口:
1.只支持监控方法的每次响应时间和一定时间内的平均响应时间

出口:
1.支持本地log4j打印输出到本地日志
2.支持输出到redis

配置参考:
redisPusher的logRedisTemplate依赖spring的org.springframework.data.redis.core.RedisTemplate

<code><bean id="redisPusher" class="com.jutem.log.collector.pusher.RedisPusher">
	<constructor-arg ref="logRedisTemplate" />
	<constructor-arg name="redisKey" value="logstash-elasticsearch" />
</bean>
<bean id="logPusher" class="com.jutem.log.collector.pusher.LogPusher"></bean>
<bean id="logCollector" class="com.jutem.log.collector.aop.LogCollector">
	<constructor-arg name="app" value="elasticsearch"></constructor-arg>
	<constructor-arg name="host" value="127.0.0.1"></constructor-arg>
	<!-- <constructor-arg ref="redisPusher"></constructor-arg> -->
	<!-- <constructor-arg ref="logPusher"></constructor-arg> -->
</bean></code>

