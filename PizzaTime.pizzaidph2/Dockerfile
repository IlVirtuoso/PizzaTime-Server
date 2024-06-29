FROM docker.io/maven:latest as builder
LABEL authors="drfaust"
WORKDIR /build
COPY . .
RUN mvn package -Dmaven.test.skip

FROM docker.io/bellsoft/liberica-openjre-debian:latest as RUNNER
WORKDIR app
ENV server.port=8080
COPY --from=builder /build/target/PizzaIdp.jar  .
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/PizzaIdp.jar"]
