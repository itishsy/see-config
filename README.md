# see-config
分布式配置中心


1，要求:
	1，jdk版本在1.8以上
	2，spring版本在4.0以上

2，使用说明：
    1、在pom.xml文件中引入依赖
      <dependency>
          <groupId>com.seebon</groupId>
          <artifactId>see-config</artifactId>
          <version>1.0</version>
      </dependency>

	2、在spring上下文配置文件中引入如下内容：
	<bean class="com.seebon.config.spring.SeeConfigPropertyPlaceholderConfigurer">
        <property name="systemPropertiesModeName" value="SYSTEM_PROPERTIES_MODE_OVERRIDE"/>
        <property name="ignoreUnresolvablePlaceholders" value="true"/>
        <property name="ignoreResourceNotFound" value="true"/>
        <property name="order" value="4"/>
        <property name="locations">
            <list>
                <value>classpath*:app.properties</value>
                <value>file:${user.home}/.spfcore/see-config.properties</value>
            </list>
        </property>
    </bean>

    3、在app.properties文件中引入如下内容：
        #是否启用配置中心，默认：true
        see.config.client.enabled=true
        #当前环境
        see.config.client.profile=dev
        #应用名
        see.config.client.appName=module-system
        #zookeeper
        see.config.client.hosts=127.0.0.1:2181

    4、通过JVM参数外部设置配置(优先使用)
        -Dsee.config.client.enabled=true
        -Dsee.config.client.profile=dev
        -Dsee.config.client.appName=module-system
        -Dsee.config.client.hosts=127.0.0.1:2181

	5、获取实时配置
	    配置变更后会实时下发到应用，可以通过以下方式实时读取最新配置.

        SeeConfigUtils 实时获取

        @Value("${key}") 实时获取

        Environment 实时获取

