# 在线学习平台

一个基于 Vue3 + Spring Boot 的在线网课学习平台，支持课程浏览、视频播放、学习进度记录、评论互动等功能。

## 技术栈

- **前端**：Vue 3 + Element Plus + Vue Router + Axios
- **后端**：Spring Boot 3.2 + Spring Security + JWT + MyBatis-Plus
- **数据库**：MySQL 8.0 + Redis
- **构建工具**：Maven（后端）/ Vue CLI（前端）

## 项目结构

```
OnlineLearningPlatform/
├── online-learning-backend/     # Spring Boot 后端项目
│   ├── src/main/java/           # Java 源代码
│   ├── src/main/resources/      # 配置文件
│   │   ├── db/schema.sql        # 数据库初始化脚本
│   │   └── application.yml      # 应用配置
│   └── pom.xml                  # Maven 配置
│
└── online-learning-frontend/    # Vue3 前端项目
    ├── src/
    │   ├── api/                 # API 接口封装
    │   ├── views/               # 页面组件
    │   ├── router/              # 路由配置
    │   └── utils/request.js     # Axios 请求封装
    └── package.json             # npm 配置
```

## 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+（可选，用于缓存）
- Maven 3.8+

## 快速开始

### 1. 克隆项目

```bash
git clone <项目地址>
cd OnlineLearningPlatform
```

### 2. 初始化数据库

登录 MySQL，执行数据库初始化脚本：

```bash
mysql -u root -p
```

在 MySQL 命令行中：

```sql
-- 设置字符编码（重要！避免中文乱码）
SET NAMES utf8mb4;

-- 执行初始化脚本
source online-learning-backend/src/main/resources/db/schema.sql
```

> **注意**：如果之前执行过脚本但失败了，建议先删除数据库重新创建：
> ```sql
> DROP DATABASE IF EXISTS online_learning;
> source online-learning-backend/src/main/resources/db/schema.sql
> ```

### 3. 启动后端服务

```bash
cd online-learning-backend

-- 编译并运行
mvn clean compile spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`

**常见问题**：
- 如果遇到 `Invalid value type for attribute 'factoryBeanObjectType'` 错误，请检查 `pom.xml` 中 MyBatis-Plus 版本是否为 3.5.6+
- 确保 MySQL 服务已启动且配置正确

### 4. 启动前端服务

```bash
cd online-learning-frontend

-- 安装依赖（首次运行需要）
npm install

-- 启动开发服务器
npm run serve
```

前端服务默认运行在 `http://localhost:3000`

**常见问题**：
- 如果 `npm install` 速度慢，可以使用淘宝镜像：`npm install --registry=https://registry.npmmirror.com`
- 确保 Vue CLI 已安装：`npm install -g @vue/cli`

## 功能模块

### 用户模块
- 用户注册 / 登录
- JWT  token 认证
- 个人中心信息管理

### 课程模块
- 课程分类浏览
- 课程搜索
- 课程详情查看
- 视频在线播放（支持倍速、进度记忆）

### 互动模块
- 课程收藏
- 评论与评分
- 学习进度记录

### 管理模块
- 课程管理（增删改查）
- 章节管理
- 视频上传
- 用户管理

## 默认账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 拥有所有管理权限 |

## 开发配置

### 后端配置

后端配置文件位于 `online-learning-backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_learning?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

请根据本地 MySQL 配置修改用户名和密码。

### 前端配置

前端代理配置位于 `online-learning-frontend/vue.config.js`：

```javascript
devServer: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    },
    '/uploads': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

## 部署说明

### 后端部署

```bash
cd online-learning-backend
mvn clean package
java -jar target/online-learning-backend-1.0.0.jar
```

### 前端部署

```bash
cd online-learning-frontend
npm run build

```
npm run serve
```
构建后的静态文件位于 `dist/` 目录，可部署到 Nginx 或其他 Web 服务器。

## 注意事项

1. **数据库编码**：确保 MySQL 客户端编码为 utf8mb4，避免中文乱码
2. **文件上传**：视频上传功能需要确保服务器有足够的存储空间
3. **跨域问题**：开发环境已通过代理解决，生产环境需配置 CORS
4. **JWT Secret**：生产环境请修改 `application.yml` 中的 `jwt.secret` 配置

## 许可证

MIT License