FROM gradle:7.3.3-jdk11
USER root

EXPOSE 8081
COPY . .
WORKDIR .
RUN gradle build
CMD ["java", "-jar", "build/libs/real-time-chat-db-1.0-SNAPSHOT.jar"]