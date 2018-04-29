FROM tomcat:8.5.12-jre8

COPY target/tio_backend.war /usr/local/tomcat/webapps/

WORKDIR /usr/local/tomcat/bin

EXPOSE 3000

CMD ["catalina.sh", "run"]