FROM mysql:5.7
COPY setup.sql /docker-entrypoint-initdb.d/
COPY charset.cnf /etc/mysql/conf.d/
ENV MYSQL_ROOT_PASSWORD=root
