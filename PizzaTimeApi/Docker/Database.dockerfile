FROM postgres
ENV POSTGRES_PASSWORD Pizza1234!
COPY ./*.sql /docker-entrypoint-initdb.d/
RUN ls -latr /docker-entrypoint-initdb.d/
EXPOSE 5432