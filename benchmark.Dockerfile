# syntax=docker/dockerfile:1

FROM amazoncorretto:18
WORKDIR /benchmark
COPY build/libs/benchmark-1.0-SNAPSHOT.jar ./
ENTRYPOINT ["java", "-jar", "benchmark-1.0-SNAPSHOT.jar"]
