FROM tomcat:8-jre11
# Copy to images tomcat path

CMD rm -rf /usr/local/tomcat/webapps
ADD ./target/view.war /usr/local/tomcat/webapps/

EXPOSE 8080
