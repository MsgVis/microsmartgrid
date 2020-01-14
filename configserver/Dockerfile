FROM java:11-jre

ADD ./target/configserver.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/configserver.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8888/actuator/health || exit 1

EXPOSE 8888
