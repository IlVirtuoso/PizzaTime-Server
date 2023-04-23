FROM mcr.microsoft.com/dotnet/sdk:6.0

RUN mkdir /opt/pizzaservice && mkdir /home/compilation && apt update && apt install npm -y && npm install -g vite
ADD ./PizzaTimeService.sln /home/compilation
ADD ./PizzaTimeApi /home/compilation/PizzaTimeApi
ADD ./PizzaTimeSPA /home/compilation/PizzaTimeSPA
RUN cd /home/compilation/PizzaTimeSPA && npm install && npm run build
RUN cd /home/compilation && ls && dotnet publish -c Release \
 && cp /home/compilation/PizzaTimeApi/bin/Release/net6.0/publish/* /opt/pizzaservice \
 && ln /opt/pizzaservice/PizzaTimeApi /usr/bin \
 && chmod +x /opt/pizzaservice/PizzaTimeApi\
 && useradd -ms /bin/sh pizzaservice \
EXPOSE 5001
USER pizzaservice
ENTRYPOINT [ "/opt/pizzaservice/PizzaTimeApi" ]

