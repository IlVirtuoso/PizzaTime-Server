FROM docker.io/gradle:latest as builder
LABEL authors="drfaust"
WORKDIR /build
COPY . .
RUN gradle bootWar

FROM docker.io/bellsoft/liberica-openjre-debian:latest as RUNNER
WORKDIR app
COPY --from=builder /build/build/OrderService.war .
ENV server.port=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/OrderService.war"]
