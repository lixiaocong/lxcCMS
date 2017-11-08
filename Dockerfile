FROM openjdk:8-jdk-alpine
ADD target/lxccms-3.0.jar app.jar
ENV JAVA_OPTS=""
ENV CMS_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar $CMS_OPTS
