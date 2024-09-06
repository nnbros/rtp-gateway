FROM openjdk:17-alpine
ARG BOT_TOKEN
ENV RTP_BOT_TOKEN=${BOT_TOKEN}
COPY ./target/*.jar /app/rtp-gateway.jar
RUN chmod 777 /app/rtp-gateway.jar
RUN --mount=type=secret,id=BOT_KEY \
  mkdir /app/certificates && cp /run/secrets/BOT_KEY /app/certificates/rtpbot.key
RUN ls /app/certificates
RUN cat /app/certificates/rtpbot.key
ENTRYPOINT ["java", "-jar", "/app/rtp-gateway.jar"]
