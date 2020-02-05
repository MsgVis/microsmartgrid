FROM openjdk:11

ADD ./target/timescaleDbWriter.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/timescaleDbWriter.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:4721/actuator/health || exit 1

EXPOSE 4721
