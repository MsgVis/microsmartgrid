FROM java:11-jre

ADD ./target/view.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/view.jar"]

EXPOSE 6000
