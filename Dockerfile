FROM openjdk:17-alpine
COPY ./target/*.jar /app/rtp-gateway.jar
RUN chmod 777 /app/rtp-gateway.jar
RUN --mount=type=secret,id=BOT_KEY \
  mkdir /app/certificates && cp /run/secrets/BOT_KEY /app/certificates/rtpbot.key
RUN --mount=type=secret,id=BOT_TOKEN \
  export RTP_BOT_TOKEN=$(cat /run/secrets/BOT_TOKEN)
ENTRYPOINT ["java", "-jar", "/app/rtp-gateway.jar"]
