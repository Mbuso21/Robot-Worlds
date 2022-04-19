FROM debian:buster-slim

RUN apt-get update
RUN apt-get install -y openjdk-11-jre curl

ADD target/serverclient-1.0-jar-with-dependencies.jar /srv/ServerClient-1.0.jar

WORKDIR /srv
EXPOSE 5050
CMD ["java", "-jar", "ServerClient-1.0.jar"]