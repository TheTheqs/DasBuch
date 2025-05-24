# Etapa 1: Build com Vite
FROM node:20-alpine AS builder
WORKDIR /app
COPY . .

# 🔽 COPIA A VARIÁVEL DE AMBIENTE PRA O BUILD
ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL
RUN npm install
RUN npm run build

# Etapa 2: Servir com NGINX
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
