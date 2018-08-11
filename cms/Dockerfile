FROM openjdk:8-jre-alpine

RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*

ADD target/lxccms-1.0.jar app.jar
ENTRYPOINT exec java -jar /app.jar
