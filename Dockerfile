FROM openjdk:17
EXPOSE 8080
RUN mkdir -p /app/
COPY build/libs/nlpdetect-0.0.1-SNAPSHOT.jar /app/nlpdetect-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/nlpdetect-0.0.1-SNAPSHOT.jar"]
