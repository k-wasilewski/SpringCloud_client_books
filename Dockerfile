FROM openjdk:8-jdk-alpine

MAINTAINER Kuba Wasilewski <k.k.wasilewski@gmail.com>

VOLUME /tmp

EXPOSE 8085

ARG JAR_FILE=target/client_books-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} client_books.jar

ENTRYPOINT ["java","-jar","/client_books.jar"]
