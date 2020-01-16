FROM tomcat:8-jre11
# Copy to images tomcat path

RUN   rm -rf /usr/local/tomcat/webapps/ROOT
ADD ./target/view.war /usr/local/tomcat/webapps/ROOT.war
CMD [ "catalina.sh", "run" ]

EXPOSE 8080
