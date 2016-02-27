#uncode-session


非常小巧的集群session共享组件，代码千行以内，避免使用应用容器插件的多种烦恼。



# 功能概述

1. 非常小巧的集群session公享组件，类似于spring-session。
2. 总代码不超过1000行。
3. 易于使用和扩展。




------------------------------------------------------------------------

# 配置

## 1. web.xml
	
	<!-- 会话共享过滤器，注意放在其他filter之前 -->
	<filter>
	    <filter-name>SessionSharingFilter</filter-name>
	    <filter-class>cn.uncode.session.SessionSharingFilter</filter-class>
	</filter>
	<filter-mapping>
	    <filter-name>SessionSharingFilter</filter-name>
	    <url-pattern>/*</url-pattern>
	</filter-mapping>

## 2. 基于Redis的Spring配置


	<!-- 配置Redis缓存池（默认基于redis实现，所以只需要配置缓存池就可以了） -->
<<<<<<< HEAD

	<bean id="cachePool" class="cn.uncode.session.data.redis.RedisSentinelPool">
=======
	<bean id="redisSentinelPool" class="cn.uncode.session.data.redis.RedisSentinelPool">
>>>>>>> 58c22ae632334d37423c07f378e242ffea23648e
		<property name="hosts">
    		<list>
    			<value>127.0.0.1:26379</value>
    			<value>127.0.0.2:26379</value>
    		</list>
    	</property>
    	<property name="auth" value="123456" />
		<property name="maxIdle" value="5" />
		<property name="maxTotal" value="20" />
		<property name="maxWaitMillis" value="10000" />
		<property name="testOnBorrow" value="true" />
	</bean>
	
## 3. 基于Memcached的Spring配置
	
	<bean id="cachePool" class="cn.uncode.session.data.memcached.MemcachedPool">
        <property name="hosts">
            <list>
                <value>127.0.0.1:11211</value>
            </list>
        </property>
    </bean>
------------------------------------------------------------------------
	
# 自定义扩展


## 1. 自定义实现类

	public class CustomSessionCache implements SessionCache{
	
		@Override
		public void put(String sessionId, SessionMap sessionMap, int timeout) {
		
		}
	
		@Override
		public SessionMap get(String sessionId) {
		
		}
	
		@Override
		public void setMaxInactiveInterval(String sessionId, int interval) {
		
		}
	
		@Override
		public void destroy(String sessionId) {
		
		}
	}
	

## 2. 配置管理器


	<!-- 配置缓存 -->
	<bean id="customSessionCache" class="cn.uncode.session.*.*.CustomSessionCache" />
	
	<!-- 配置会话缓存管理器 -->
	<bean id="sessionCacheManager" class="cn.uncode.session.data.SessionCacheManager">
		<property name="sessionCache" ref="customSessionCache" />
		<!-- 或者使用以下配置，二选一 -->
		<!--
		<property name="beanName" value="customSessionCache" />
		-->
	</bean>

------------------------------------------------------------------------


# 版权

作者：冶卫军（ywj_316@qq.com）
贡献开发：马煜

技术支持QQ群：47306892

Copyright 2016 www.uncode.cn
