FROM docker.io/kong:latest
USER root

RUN mkdir /opt/config
COPY --chown=kong:kong ./docker-kong-master/compose/config/kong.yaml /opt/config/kong.yaml 
ENV KONG_DECLARATIVE_CONFIG=/opt/config/kong.yaml


HEALTHCHECK --interval=60s --timeout=10s --retries=10 CMD kong-health
USER kong
ENV KONG_DECLARATIVE_CONFIG=/opt/config/kong.yaml
ENV KONG_ADMIN_ACCESS_LOG=/dev/stdout
ENV KONG_ADMIN_ERROR_LOG=/dev/stderr
ENV KONG_PROXY_LISTEN=0.0.0.0:8000
ENV KONG_ADMIN_LISTEN=0.0.0.0:8001
ENV KONG_ADMIN_GUI_LISTEN=0.0.0.0:8002
ENV KONG_PROXY_ACCESS_LOG=/dev/stdout
ENV KONG_PROXY_ERROR_LOG=/dev/stderr
ENV KONG_DATABASE=off