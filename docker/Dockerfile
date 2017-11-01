FROM base:2 
VOLUME /config
COPY dockerRun.sh .
RUN git clone https://github.com/lixiaocong/lxcCMS.git
CMD ["sh", "./dockerRun.sh"]
