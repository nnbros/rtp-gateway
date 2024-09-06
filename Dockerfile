FROM openjdk:17-alpine
COPY ./target/*.jar /app/rtp-gateway.jar
RUN chmod 777 /app/rtp-gateway.jar
RUN ls -al /app
RUN ls -al
ENTRYPOINT ["java", "-jar", "/app/rtp-gateway.jar"]
