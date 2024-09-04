FROM openjdk:17-alpine
WORKDIR /app
COPY /target/*.jar /app/rtp-gateway.jar
RUN chmod 777 /app/rtp-gateway.jar
ENTRYPOINT ["/app/rtp-gateway.jar"]
