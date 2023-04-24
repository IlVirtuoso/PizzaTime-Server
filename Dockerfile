FROM mcr.microsoft.com/dotnet/sdk:6.0
ENV TERM xterm-color
ARG DEBIAN_FRONTEND=noninteractive
RUN mkdir /home/nodeInstall && apt update && apt install build-essential -y
ADD https://nodejs.org/dist/v18.16.0/node-v18.16.0-linux-x64.tar.xz /home/nodeInstall/node.tar.xz
RUN cd /home/nodeInstall/ \
    && tar -vxf ./node.tar.xz \
    && cd ./node-v18.16.0-linux-x64 
ENV PATH=$PATH:/home/nodeInstall/node-v18.16.0-linux-x64/bin/
RUN mkdir /opt/pizzaservice \
&& mkdir /home/compilation\
&& npm install -g vite
ADD ./PizzaTimeService.sln /home/compilation
ADD ./PizzaTimeApi /home/compilation/PizzaTimeApi
ADD ./PizzaTimeSPA /home/compilation/PizzaTimeSPA
RUN cd /home/compilation/PizzaTimeSPA && npm install && npm run build
RUN cd /home/compilation && dotnet dev-certs https --trust && dotnet publish -c Release \
 && cp -r /home/compilation/PizzaTimeApi/bin/Release/net6.0/publish/* /opt/pizzaservice \
 && ln /opt/pizzaservice/PizzaTimeApi /usr/bin \
 && chmod +x /opt/pizzaservice/PizzaTimeApi
RUN useradd -ms /bin/sh pizzaservice 
EXPOSE 5000
EXPOSE 5001
USER pizzaservice
ENTRYPOINT [ "/opt/pizzaservice/PizzaTimeApi" ]

