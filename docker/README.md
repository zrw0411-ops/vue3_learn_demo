# Docker 一键启动 OA 系统

## 快速启动

```bash
cd /home/node/.openclaw/workspace/oa-system/docker

# 构建并启动所有服务（MySQL + 后端 + 前端）
docker-compose up -d --build
```

## 启动后访问

| 服务 | 地址 |
|------|------|
| 前端界面 | http://localhost:3000 |
| 后端 API | http://localhost:8080 |
| MySQL | localhost:3306（root/root123） |

## 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 管理员 |
| zhangsan | 123456 | 主管 |
| lisi | 123456 | HR |
| wangwu | 123456 | 员工 |

## 常用命令

```bash
# 查看运行状态
docker-compose -f docker-compose.yml ps

# 查看后端日志
docker logs -f oa-backend

# 查看前端日志
docker logs -f oa-frontend

# 查看 MySQL 日志
docker logs -f oa-mysql

# 停止所有服务
docker-compose -f docker-compose.yml down

# 重新构建并启动
docker-compose -f docker-compose.yml up -d --build

# 清理（删除容器+卷）
docker-compose -f docker-compose.yml down -v
```

## 数据持久化

MySQL 数据存储在 Docker volume 中，删除容器不会丢失数据。

如需重置数据库：
```bash
docker-compose -f docker-compose.yml down -v
docker-compose -f docker-compose.yml up -d --build
```

## 前端访问后端 API

前端 nginx 已配置 `/api/` 代理到后端 `http://backend:8080/`，
无需额外配置 CORS。
