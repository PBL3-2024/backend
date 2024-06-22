FROM --platform=linux/arm64 maven:3.9.6-eclipse-temurin-21 as build
WORKDIR /workspace/app

COPY . /workspace/app

RUN --mount=type=cache,target=/root/.m2 mvn -Djavacpp.platform=linux-arm64 clean install -DskipTests -P production

FROM --platform=linux/arm64 eclipse-temurin:21-jdk
WORKDIR /application
ARG BUILDDIR=/workspace/app/target
COPY --from=build ${BUILDDIR}/*.jar ./
RUN addgroup --system --gid 1002 app && adduser --system --uid 1002 --gid 1002 appuser
USER 1002
ENTRYPOINT ["java","-jar","*.jar"]