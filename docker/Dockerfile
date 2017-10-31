FROM base 
VOLUME /config
COPY dockerRun.sh .
RUN git clone https://github.com/lixiaocong/lxcCMS.git
CMD ["sh", "./dockerRun.sh"]
