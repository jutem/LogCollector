# LogCollector
通过注释收集日志到指定地址

0.0.1-snapshot
### 入口:
	1.只支持监控方法的每次响应时间和一定时间内的平均响应时间

### 出口:
	1.支持本地log4j打印输出到本地日志
	2.支持输出到redis

### 配置参考:
	redisPusher的logRedisTemplate依赖spring的org.springframework.data.redis.core.RedisTemplate

	<bean id="redisPusher" class="com.jutem.log.collector.pusher.RedisPusher">
		<constructor-arg ref="logRedisTemplate" />
		<constructor-arg name="redisKey" value="logstash-elasticsearch" />
	</bean>
	<bean id="logPusher" class="com.jutem.log.collector.pusher.LogPusher"></bean>
	<bean id="logCollector" class="com.jutem.log.collector.aop.LogCollector">
		<constructor-arg name="app" value="elasticsearch"></constructor-arg>
		<constructor-arg name="host" value="127.0.0.1"></constructor-arg>
		<!-- <constructor-arg ref="redisPusher"></constructor-arg> -->
		<!-- <constructor-arg ref="logPusher"></constructor-arg> -->
	</bean>

### logstash相关:
	2016.06.03 更新logstash简单的配置文件，需要修改为自己的redis地址和elasticsearch地址。这个配置文件会提交处理好的日志到elasticsearch中
	2016.6.15 logstash可以发送elasticsearch模板

### elasticsearch相关:
	2016.06.15 新增mapping配置

