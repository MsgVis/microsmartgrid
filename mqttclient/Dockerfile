FROM openjdk:11

ADD ./target/mqttclient.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/mqttclient.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:1883/actuator/health || exit 1

EXPOSE 1883
