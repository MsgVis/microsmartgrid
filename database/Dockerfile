FROM openjdk:11

ADD ./target/database.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/database.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:4711/actuator/health || exit 1

EXPOSE 4711
