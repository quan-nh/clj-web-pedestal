`logback.xml`
```xml
<configuration>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} %X{io.pedestal} - %msg%n
            </pattern>
        </encoder>
    </appender>

    <logger name="clj-web-pedestal" level="DEBUG" additivity="false">
        <appender-ref ref="CONSOLE"/>
    </logger>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>

</configuration>
```

then we can use that
```clj
(:require [io.pedestal.log :as log])

(log/debug :req req)
```
