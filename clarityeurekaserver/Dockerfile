FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY ./target/libs/clarityeurekaserver-*.jar clarityeurekaserver.jar
EXPOSE 8767
CMD java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar clarityeurekaserver.jar
