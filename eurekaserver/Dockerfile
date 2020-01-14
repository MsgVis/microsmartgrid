FROM openjdk:11

ADD ./target/eurekaserver.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/eurekaserver.jar"]

EXPOSE 8761
