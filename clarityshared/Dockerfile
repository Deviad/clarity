FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim as builder
ADD . .
RUN ./gradlew clean build
