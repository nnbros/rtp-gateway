FROM centos:latest
RUN yum update -y && \
    yum install -y java-17-openjdk-devel
WORKDIR /app
COPY /target/*.jar /app/rtp-gateway.jar
RUN chmod 777 /app/rtp-gateway.jar
ENTRYPOINT ["/app/rtp-gateway.jar"]
