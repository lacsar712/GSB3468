# Fishfrineds 钓鱼社区

## 项目类型
- 类型：A) FULLSTACK_WEB

## 技术栈
- 前端（公众端）：Vue.js 3 + Vite（模块：`www`）
- 前端（管理端）：Vue.js 3 + Vite + Element Plus（模块：`fishback`）
- 后端：Spring Boot 3.2 + Spring Security + JPA
- 数据库：MySQL 8.0（utf8mb4）

## 运行方式 / 快速开始

### 前置条件
- Docker Desktop / Docker Engine 已运行
- 端口 6401-6404 未被占用

### 环境设置
```bash
# 复制环境变量模板（可选，默认值已在 docker-compose.yml 中提供）
cp .env.example .env
```

### 启动服务
```bash
# 一键启动所有服务
docker compose up

# 或使用后台模式
docker compose up -d
```

### 访问地址
- **前台站点（www）**：http://localhost:6401
- **管理后台（fishback）**：http://localhost:6402
- **后端 API**：http://localhost:6403
- **健康检查**：http://localhost:6403/api/health

## 测试账号
- 用户名：`admin`
- 密码：`password123`
- 角色：ADMIN

> 说明：登录页不会明文显示账号密码。可点击 `Use Test Account` 按钮自动填充。

## 服务列表

本项目包含以下 4 个服务：

| 服务 | 说明 | 容器端口 | 主机端口 | 地址 |
|---------|-------------|---------------|-----------|-----|
| www | 公开前台（Vue.js 3） | 80 | 6401 | http://localhost:6401 |
| fishback | 后台管理（Vue.js 3） | 80 | 6402 | http://localhost:6402 |
| backend | REST API（Spring Boot） | 8000 | 6403 | http://localhost:6403 |
| db | MySQL 8.0 数据库 | 3306 | 6404 | localhost:6404 |

## 端口映射（RUN_SLOT=34）

- **www**：6401 -> 80
- **fishback**：6402 -> 80
- **backend**：6403 -> 8000
- **db**：6404 -> 3306

## API 接口

### 公开接口（GET）
- `GET /api/spots` - 钓点列表
- `GET /api/spots/{id}` - 钓点详情
- `GET /api/catches` - 鱼获列表
- `GET /api/catches/{id}` - 鱼获详情
- `GET /api/life` - 生活分享列表
- `GET /api/life/{id}` - 生活分享详情
- `GET /api/friends` - 钓友信息列表
- `GET /api/friends/{id}` - 钓友详情
- `GET /api/gear` - 钓具列表
- `GET /api/gear/{id}` - 钓具详情
- `GET /api/gear/category/{category}` - 按分类查询钓具

### 受保护接口（需要认证）
- `POST /api/spots` - 创建钓点
- `PUT /api/spots/{id}` - 更新钓点
- `DELETE /api/spots/{id}` - 删除钓点
- `POST /api/catches` - 创建鱼获
- `PUT /api/catches/{id}` - 更新鱼获
- `DELETE /api/catches/{id}` - 删除鱼获
- `POST /api/life` - 创建生活分享
- `PUT /api/life/{id}` - 更新生活分享
- `DELETE /api/life/{id}` - 删除生活分享
- `POST /api/friends` - 创建钓友信息
- `PUT /api/friends/{id}` - 更新钓友信息
- `DELETE /api/friends/{id}` - 删除钓友信息
- `POST /api/gear` - 创建钓具信息
- `PUT /api/gear/{id}` - 更新钓具信息
- `DELETE /api/gear/{id}` - 删除钓具信息

## 功能清单
- [x] F1：钓点发布与浏览（R003）
- [x] F2：鱼获分享与浏览（R004）
- [x] F3：生活分享与浏览（R005）
- [x] F4：钓友资料分享与浏览（R006）
- [x] F5：钓具交易与浏览（R007）
- [x] F6：认证与授权（R011）
- [x] F7：登录页安全性（不明文展示凭据）（R013）
- [x] F8：Docker Compose 一键启动（R012）
- [x] F9：MySQL utf8mb4 字符集验证（R015）
- [x] F10：中文 UTF-8 显示验证（R014）

## 验证说明

### 快速验证（3-5 步）

1. **启动服务**
   ```bash
   docker compose up
   ```

2. **验证所有服务运行中**
   ```bash
   docker compose ps
   # 预期：4 个服务（db、backend、www、fishback）状态均为 "Up"
   ```

3. **测试后端健康检查**
   ```bash
   curl http://localhost:6403/api/health
   # 预期：{"status":"UP","version":"1.0.0"}
   ```

4. **测试公开接口**
   ```bash
   curl http://localhost:6403/api/spots
   curl http://localhost:6403/api/catches
   curl http://localhost:6403/api/life
   ```

5. **验证前端页面**
   - 打开 http://localhost:6401（公开前台）
   - 打开 http://localhost:6402/login（管理后台登录页）
   - 点击“使用测试账号”按钮，确认页面未明文展示账号密码

### 管理员登录验证

**成功路径：**
1. 打开 http://localhost:6402/login
2. 点击“使用测试账号”按钮（自动填充 admin/password123）
3. 点击“登录”按钮
4. 预期：跳转到 Dashboard 页面，token 存入 localStorage

**API 测试：**
```bash
curl -X POST http://localhost:6403/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'
# 预期：返回 200 OK，响应包含 token/tokenType/username/role/message 字段
```

**失败路径：**
```bash
curl -X POST http://localhost:6403/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrongpassword"}'
# 预期：返回 401 Unauthorized，并包含错误信息
```

### 首页导航验证

1. 打开 http://localhost:6401/
2. 依次点击每个功能卡片：
   - **钓点信息** -> 应跳转到 `/spots`
   - **鱼获分享** -> 应跳转到 `/catches`
   - **生活分享** -> 应跳转到 `/life`
   - **钓友圈** -> 应跳转到 `/friends`
   - **钓具交易** -> 应跳转到 `/gear`
3. 所有卡片支持键盘导航（Tab + Enter/Space）

### 数据库字符集验证
```bash
docker exec fishfrineds-db mysql -uroot -prootpass123 -e "SHOW VARIABLES LIKE 'character_set_%';"
# 预期：所有 character_set_* 变量均为 utf8mb4
```

### 端口映射验证
| 服务 | 端口 | 状态检查 |
|---------|------|--------------|
| www | 6401 | `curl -I http://localhost:6401` |
| fishback | 6402 | `curl -I http://localhost:6402` |
| backend | 6403 | `curl http://localhost:6403/api/health` |
| db | 6404 | `docker exec fishfrineds-db mysqladmin ping` |

## RUN_SLOT
- RUN_SLOT：34
- COMPOSE_PROJECT_NAME：label-3468-slot34
- 证据目录：evidence/run-slot34/

## QC 提交包
- `deliverables/min-run-package.md` - 最小运行包文件清单
- `deliverables/original-requirements-artifacts.md` - 原始需求产物映射
- `QA_REPORT.md` - 六维 QA 报告
- `requirements/req-lock.md` - R-ID 需求冻结与状态
