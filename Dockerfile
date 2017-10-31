FROM ubuntu:xenial
COPY * /src/
RUN 	   apt-get update \
	&& apt-get install -y nginx openjdk-8-jdk nodejs tomcat8 python-pip python-dev build-essential \
	&& pip install shadowsocks
CMD ["ssserver", "-c", "/src/ss.config"]
