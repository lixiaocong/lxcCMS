FROM openjdk:8-jdk
ADD target/lxccms-1.0.jar app.jar

ENV JAVA_OPTS=""
ENV APPLICATION_URL=""

ENV QQ_ID=""
ENV QQ_SECRET=""

ENV WEIXIN_ID=""
ENV WEIXIN_SECRET=""
ENV WEIXIN_TOKEN=""
ENV WEIXIN_KEY=""

ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar --spring.profiles.active=product --application.url=$APPLICATION_URL --qq.id=$QQ_ID --qq.secret=$QQ_SECRET --weixin.id=$WEIXIN_ID --weixin.secret=$WEIXIN_SECRET --weixin.token=$WEIXIN_TOKEN  --weixin.key=$WEIXIN_KEY
