FROM --platform=linux/x86_64 maven:3.9.6-eclipse-temurin-21 as build
WORKDIR /workspace/app

COPY ./src /workspace/app/src
COPY ./pom.xml /workspace/app

RUN --mount=type=cache,target=/root/.m2 mvn -Djavacpp.platform=linux-x86_64 clean install -DskipTests -P production
ARG JAR_FILE=target/*.jar
RUN java -Djarmode=layertools -jar ${JAR_FILE} extract --destination target/extracted

FROM --platform=linux/x86_64 eclipse-temurin:21-jdk
WORKDIR /application
ARG EXTRACTED=/workspace/app/target/extracted
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./
RUN addgroup --system --gid 1002 app && adduser --system --uid 1002 --gid 1002 appuser
USER 1002
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]