FROM docker.io/maven:latest as builder
LABEL authors="drfaust"
WORKDIR /build
COPY . .
RUN mvn package -Dmaven.test.skip

FROM docker.io/bellsoft/liberica-openjre-debian:latest as RUNNER
WORKDIR app
COPY --from=builder /build/target/PizzaEngine.jar .

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/PizzaEngine.jar"]
