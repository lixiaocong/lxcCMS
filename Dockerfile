FROM openjdk:8-jdk-alpine
ADD target/lxccms-3.0.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar
