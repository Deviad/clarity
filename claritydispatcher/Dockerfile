FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim as builder
ADD . /app
WORKDIR /app
RUN ./gradlew clean build install -Pprofile=garden


FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
EXPOSE 8887
CMD java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar claritydispatcher.jar
