FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ARG PROFILE
ENV envPROFILE=$PROFILE

ENTRYPOINT ["java","-Xmx1024m", "-jar", "app.jar","--spring.profiles.active=${envPROFILE}"]