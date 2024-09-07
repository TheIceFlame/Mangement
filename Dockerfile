#
# Build stage
#
FROM maven:3.8.2-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
# Adjust the path if the JAR file is named differently or located elsewhere
COPY --from=build /target/*.jar mangment.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","mangment.jar"]