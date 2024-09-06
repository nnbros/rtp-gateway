FROM openjdk:17-alpine
ARG BOT_TOKEN
ENV RTP_BOT_TOKEN=${BOT_TOKEN}
COPY ./target/*.jar /app/rtp-gateway.jar
RUN chmod 777 /app/rtp-gateway.jar
RUN mkdir /app/certificates
RUN --mount=type=secret,id=BOT_KEY,target=/app/certificates/rtpbot.key
RUN ls /app/certificates
RUN cat /app/certificates/rtpbot.key
ENTRYPOINT ["java", "-jar", "/app/rtp-gateway.jar"]
