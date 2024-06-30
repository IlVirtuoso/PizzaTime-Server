FROM docker.io/node:latest as build 
WORKDIR /app 
COPY . . 
RUN npm run build --prod 
FROM docker.io/nginx:alpine

COPY --from=build /app/dist/pizza-time-frontend/browser /usr/share/nginx/html
# Copy custom nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/frontend.conf
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
