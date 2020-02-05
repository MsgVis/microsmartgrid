FROM openjdk:11

ADD ./target/timescaleDbReader.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/timescaleDbReader.jar"]

HEALTHCHECK --interval=10s --timeout=10s CMD curl -f http://localhost:4720/actuator/health || exit 1

EXPOSE 4720
