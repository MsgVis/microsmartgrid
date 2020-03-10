FROM openjdk:11

ADD ./target/eurekaserver.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/eurekaserver.jar"]

HEALTHCHECK --interval=10s --timeout=10s CMD curl -f http://localhost:8761/actuator/health || exit 1

EXPOSE 8761
