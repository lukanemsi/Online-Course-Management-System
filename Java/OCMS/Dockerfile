FROM maven:3.9.9-eclipse-temurin-21
WORKDIR /app
EXPOSE 8080
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests
CMD ["java", "-jar", "./target/OCMS.jar"]