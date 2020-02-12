FROM openjdk:11

ADD ./target/configserver.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/configserver.jar"]

HEALTHCHECK --interval=10s --timeout=10s CMD curl -f http://localhost:8888/actuator/health || exit 1

EXPOSE 8888
