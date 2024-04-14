FROM mcr.microsoft.com/dotnet/sdk:8.0 as prebuild
ENV TERM xterm-color
ARG DEBIAN_FRONTEND=noninteractive
RUN mkdir /home/nodeInstall && apt update && apt install build-essential -y
ADD https://nodejs.org/dist/v18.16.0/node-v18.16.0-linux-x64.tar.xz /home/nodeInstall/node.tar.xz
RUN cd /home/nodeInstall/ \
    && tar -vxf ./node.tar.xz \
    && cd ./node-v18.16.0-linux-x64 
ENV PATH=$PATH:/home/nodeInstall/node-v18.16.0-linux-x64/bin/
RUN npm install -g vite
FROM prebuild as build
WORKDIR /home/builder
COPY . .
RUN dotnet build ./PizzaTimeApi/ -c Release -o build
FROM mcr.microsoft.com/dotnet/aspnet:8.0 as runner
WORKDIR /runtime
COPY --from=build /home/builder/build/* .
RUN useradd -ms /bin/sh pizzaservice 
EXPOSE 5000 5001
USER pizzaservice
ENTRYPOINT [ "./PizzaTime.Api" ]

