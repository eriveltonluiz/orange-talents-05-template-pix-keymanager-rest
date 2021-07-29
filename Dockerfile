FROM openjdk:11-jdk-slim
ARG JAR_FILE=build/libs/pix-rest-0.1-all.jar
ADD ${JAR_FILE} pix-rest.jar
ENTRYPOINT  ["java", "-jar", "/pix-rest.jar"]
EXPOSE 8081