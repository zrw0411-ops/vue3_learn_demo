# ====== 构建阶段 ======
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# 先复制 pom.xml 以利用 Maven 缓存
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源码并构建
COPY src ./src
RUN mvn clean package -DskipTests -B

# ====== 运行阶段 ======
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 创建普通用户（安全加固）
RUN addgroup -S oa && adduser -S oa -G oa

# 复制 JAR 文件
COPY --from=builder /app/target/*.jar app.jar

# 切换到普通用户
USER oa

EXPOSE 8080

# JVM 优化参数
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
