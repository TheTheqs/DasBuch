# Etapa 1: Build com Vite
FROM node:20-alpine AS builder
WORKDIR /app
COPY . .

# COPIA .env local para dentro do container
# (se preferir, pode usar echo pra criar na hora também)
COPY .env .env

RUN npm install
RUN npm run build

# Etapa 2: Servir com NGINX
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
