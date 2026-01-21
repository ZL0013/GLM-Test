# API 集成测试计划

## 目标
编写端到端集成测试，确保后端接口正确后对接前端。

## 技术方案

### 测试框架选择：MockMvc
- 使用 `@SpringBootTest` + `@AutoConfigureMockMvc`
- 完整 Spring 上下文 + 真实数据库（PostgreSQL）
- 测试 HTTP 层、安全过滤器、Controller、Service、Repository 全链路

### JWT 认证测试策略
在测试中直接复用现有 `JwtTokenManager` 生成有效 token：
```java
@Autowired
private JwtTokenManager jwtTokenManager;

String token = jwtTokenManager.generateAccessToken(
    new TokenPayload(1L, "admin", 1L, 1)
);
```

## 实现步骤

### 步骤 1：创建测试基础类

**文件**：`GLM-admin/src/test/java/com/xie/glm/test/AbstractIntegrationTest.java`

**功能**：
- 初始化 MockMvc
- 提供 JWT token 生成辅助方法
- 提供通用断言方法（assertSuccess、assertError）
- 提供 JSON 解析工具

### 步骤 2：创建测试数据工厂

**文件**：`GLM-admin/src/test/java/com/xie/glm/test/util/TestDataFactory.java`

**功能**：
- 创建测试用的 Builder 模式实体
- 预配置测试用户（admin、普通用户、禁用用户）
- 提供常见测试场景数据

### 步骤 3：添加 SQL 测试脚本

**目录**：`GLM-admin/src/test/resources/sql/`

**文件**：
- `setup.sql` - 测试数据初始化
- `cleanup.sql` - 测试数据清理

### 步骤 4：编写认证接口集成测试

**文件**：`GLM-admin/src/test/java/com/xie/glm/admin/integration/AuthControllerIntegrationTest.java`

**测试覆盖**：
| 场景 | 预期结果 |
|------|----------|
| 正确用户名密码登录 | code=0, 返回双 token |
| 错误密码登录 | code=11002 (USER_PASSWORD_ERROR) |
| 不存在的用户登录 | code=11001 (USER_NOT_FOUND) |
| 空用户名密码登录 | code=15003 (PARAM_NULL) |
| 账号已禁用用户登录 | code=11003 (USER_ACCOUNT_DISABLED) |
| 有效 refresh token 刷新 | code=0, 返回新 access token |
| 过期 refresh token 刷新 | code=10001 (UNAUTHORIZED) |
| 无效 token 刷新 | code=10001 (UNAUTHORIZED) |

### 步骤 5：编写用户管理接口集成测试

**文件**：`GLM-admin/src/test/java/com/xie/glm/admin/integration/UserControllerIntegrationTest.java`

**测试覆盖**：
| 场景 | 预期结果 |
|------|----------|
| 未认证访问 | code=10001 (UNAUTHORIZED) |
| 有认证无权限访问 | code=10002 (FORBIDDEN) |
| 分页查询用户 | code=0, 返回分页数据 |
| 根据 ID 查询用户 | code=0, 返回用户详情 |
| 创建用户（用户名重复） | code=业务错误码 |
| 创建用户（正常） | code=0, 返回创建的用户 ID |
| 更新用户 | code=0 |
| 删除用户 | code=0 |

### 附：业务错误码参考

| 错误码 | 枚举名 | 描述 |
|--------|--------|------|
| 0 | SUCCESS | 成功 |
| 10001 | UNAUTHORIZED | 未认证或令牌已过期 |
| 10002 | FORBIDDEN | 无权访问 |
| 11001 | USER_NOT_FOUND | 用户不存在 |
| 11002 | USER_PASSWORD_ERROR | 密码错误 |
| 11003 | USER_ACCOUNT_DISABLED | 账号已禁用 |
| 12002 | ROLE_NAME_DUPLICATE | 角色名称已存在 |
| 15003 | PARAM_NULL | 参数不能为空 |

### 步骤 6：扩展其他核心接口

- `RoleControllerIntegrationTest`
- `MenuControllerIntegrationTest`
- `DeptControllerIntegrationTest`

## 关键文件

| 文件 | 用途 |
|------|------|
| `GLM-framework/.../security/JwtTokenManagerImpl.java` | JWT 生成逻辑 |
| `GLM-framework/.../config/SecurityConfig.java` | 白名单：`/api/auth/login`、`/api/auth/refresh` |
| `GLM-admin/.../controller/AuthController.java` | 认证接口 |
| `GLM-admin/.../controller/UserController.java` | 用户 CRUD 接口 |
| `GLM-common/.../dto/TokenPayload.java` | Token 载荷结构 |

## 验证方式

```bash
# 运行所有集成测试
mvn test -Dtest=*IntegrationTest

# 运行特定测试类
mvn test -Dtest=AuthControllerIntegrationTest

# 运行特定测试方法
mvn test -Dtest=AuthControllerIntegrationTest#testLogin_Success
```

## 响应格式断言

统一响应格式：`{"code": 0, "message": "success", "data": {}}`

```java
// 成功响应
.andExpect(jsonPath("$.code").value(0))
.andExpect(jsonPath("$.message").value("success"))

// 失败响应
.andExpect(jsonPath("$.code").value(401))
```

## 目录结构

```
GLM-admin/src/test/
├── java/com/xie/glm/
│   ├── test/
│   │   ├── AbstractIntegrationTest.java      # 测试基类
│   │   └── util/
│   │       └── TestDataFactory.java          # 测试数据工厂
│   └── admin/integration/
│       ├── AuthControllerIntegrationTest.java
│       ├── UserControllerIntegrationTest.java
│       ├── RoleControllerIntegrationTest.java
│       └── ...
└── resources/
    └── sql/
        ├── setup.sql
        └── cleanup.sql
```
