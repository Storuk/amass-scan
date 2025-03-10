FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    unzip \
    wget

RUN wget https://github.com/OWASP/Amass/releases/download/v4.2.0/amass_linux_amd64.zip
RUN unzip amass_linux_amd64.zip
RUN mv amass_Linux_amd64/amass /usr/local/bin/
RUN rm -rf amass_linux_amd64.zip amass_Linux_amd64

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]