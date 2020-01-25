FROM openjdk:11

ADD ./target/database.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/mqttclient.jar"]

EXPOSE 4711
