FROM openjdk:11

ADD ./target/mqttclient.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/mqttclient.jar"]

EXPOSE 1883
