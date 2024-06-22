FROM maven:3.9.6-eclipse-temurin-21-alpine as build
WORKDIR /workspace/app

COPY . /workspace/app

RUN --mount=type=cache,target=/root/.m2 mvn -Djavacpp.platform=linux-amd64 clean install -DskipTests -P production
ARG JAR_FILE=target/*.jar
RUN java -Djarmode=layertools -jar ${JAR_FILE} extract --destination target/extracted

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /application
ARG EXTRACTED=/workspace/app/target/extracted
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./
RUN addgroup -S demo && adduser -S demo -G demo
USER demo
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]