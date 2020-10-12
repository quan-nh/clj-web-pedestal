FROM openjdk:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/clj-web-pedestal-0.0.1-SNAPSHOT-standalone.jar /clj-web-pedestal/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/clj-web-pedestal/app.jar"]
