FROM docker.io/maven:latest as builder
LABEL authors="drfaust"
WORKDIR /build
COPY . .
RUN ./gradlew jar

FROM docker.io/bellsoft/liberica-openjre-debian:latest as RUNNER
WORKDIR app
COPY --from=builder /build/build/OrderService.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/OrderService.jar"]
