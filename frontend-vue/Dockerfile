# ====== 构建阶段 ======
FROM node:20-alpine AS builder

WORKDIR /app

# 先复制 package.json 以利用 npm 缓存
COPY package*.json ./
RUN npm install

# 复制源码并构建
COPY . .
RUN npm run build

# ====== 运行阶段（Nginx） ======
FROM nginx:alpine

# 复制构建产物
COPY --from=builder /app/dist /usr/share/nginx/html

# 自定义 nginx 配置（支持 Vue Router history 模式）
RUN echo 'server { \
    listen 80; \
    server_name _; \
    root /usr/share/nginx/html; \
    index index.html; \
    location / { \
        try_files $uri $uri/ /index.html; \
    } \
    location /api/ { \
        proxy_pass http://backend:8080/; \
        proxy_set_header Host $host; \
        proxy_set_header X-Real-IP $remote_addr; \
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
