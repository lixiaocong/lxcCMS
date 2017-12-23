FROM openjdk:8-jdk
ADD target/lxccms-1.0.jar app.jar
ENTRYPOINT exec java -jar /app.jar
