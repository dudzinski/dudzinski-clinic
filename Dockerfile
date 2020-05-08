FROM adoptopenjdk:11-jdk-hotspot as builder
WORKDIR app
# Copy Gradle files
COPY gradle gradle
COPY gradlew ./
# Download Gradle distribution
RUN ./gradlew --version
COPY gradle.properties* settings.gradle* build.gradle* ./
# Build the project
COPY src src
RUN ./gradlew --no-daemon bootJar

FROM adoptopenjdk:11-jre-hotspot
RUN echo "Europe/Warsaw" > /etc/timezone
COPY --from=builder /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java"]
CMD ["-jar", "/app/app.jar"]
