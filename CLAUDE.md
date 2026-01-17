# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在此代码仓库中工作时提供指导。

# --- 核心原则导入 (最高优先级) ---
# 明确导入项目宪法，确保AI在思考任何问题前，都已加载核心原则。
@./constitution.md

# --- 核心使命与角色设定 ---
你是一个资深的Java语言工程师，正在协助我开发一个多模块 Spring Boot 管理系统。
你的所有行动都必须严格遵守上面导入的项目宪法。

## 项目概述

GLM-Test 是一个基于 Java 17 和 Maven 构建的多模块 Spring Boot 管理系统。

**技术栈**：
- **框架**：Spring Boot 3.5.8、Spring Security（自定义注解 + EL 表达式）
- **数据库**：PostgreSQL + MyBatis Plus 3.5.14
- **认证**：JWT 双令牌（Access Token + Refresh Token）
- **代码生成**：Lombok 1.18.38 + MapStruct 1.6.3
- **API 文档**：Knife4j 4.4.0

## 构建与测试命令

```bash
# 构建所有模块
mvn clean package

# 运行所有测试
mvn test

# 运行特定模块的测试
mvn test -pl GLM-admin
mvn test -pl GLM-system
mvn test -pl GLM-common
mvn test -pl GLM-framework

# 运行特定测试类
mvn test -Dtest=com.example.TestClassName

# 运行特定测试方法
mvn test -Dtest=com.example.TestClassName#testMethod

# 跳过测试构建
mvn clean package -DskipTests

# 安装到本地仓库
mvn clean install
```

## 模块架构

```
GLM-Test/
├── GLM-framework/   # 框架层：基础类、配置、核心组件
├── GLM-common/      # 公共层：工具类、DTO、共享接口
├── GLM-system/      # 系统层：业务逻辑、领域模型、服务
├── GLM-generator/   # 代码生成模块：数据库反向工程、代码模板引擎
└── GLM-admin/       # 管理层：Web 控制器、REST API、管理界面
```

**依赖流向**：
- `GLM-admin` 依赖 `GLM-system`
- `GLM-system` 依赖 `GLM-framework` 和 `GLM-common`
- `GLM-generator` 依赖 `GLM-framework` 和 `GLM-common`
- `GLM-framework` 依赖 `GLM-common`

**标准包结构**：
```
com.xie.glm.admin/
├── controller/      # REST API 控制器
├── resource/        # 资源装配器（DTO → VO）

com.xie.glm.system/
├── domain/          # 实体类（Entity）
├── service/         # 业务服务接口和实现
├── mapper/          # MyBatis Mapper 接口
├── repository/      # 数据访问层

com.xie.glm.framework/
├── config/          # Spring 配置类
├── security/        # 安全认证、JWT、权限校验
├── web/             # 全局异常处理、拦截器

com.xie.glm.common/
├── dto/             # 数据传输对象（请求/响应）
├── vo/              # 视图对象（前端展示）
├── bo/              # 业务对象（内部流转）
├── converter/       # MapStruct 转换器
├── enums/           # 枚举类
├── exception/       # 自定义异常
└── util/            # 工具类
```

## 开发原则

本项目遵循 `constitution.md` 中定义的严格开发规则。核心原则：

### 测试优先（不可妥协）
- 所有功能必须以失败的测试开始（红-绿-重构）
- 优先使用 `@ParameterizedTest` 配合 `@MethodSource` 或 `@CsvSource`
- 优先使用 `@SpringBootTest` 进行集成测试

**参数化测试示例**：
```java
@ParameterizedTest
@MethodSource("provideLoginData")
void testLogin(String username, String password, boolean expectedSuccess) {
    // 测试逻辑
}

private static Stream<Arguments> provideLoginData() {
    return Stream.of(
        Arguments.of("admin", "password123", true),
        Arguments.of("admin", "wrong", false),
        Arguments.of("unknown", "password123", false)
    );
}
```

### 异常处理
- 始终使用异常链：`throw new CustomException("message", cause)`
- 使用 `@ControllerAdvice` + `@ExceptionHandler` 进行统一异常处理

### 依赖注入
- 优先使用构造器注入而非字段注入
- 永远不要使用静态变量传递状态
- 所有依赖通过 Spring 容器管理

### 配置
- 使用 `@ConfigurationProperties` 进行类型安全的配置绑定
- 避免使用 `@Value` 注入多个属性

### 代码规范
- **实体类**：使用 Lombok `@Data` 注解
- **不可变数据类**：必须使用 Java 14+ 的 `record`（如 `ValidationResult`、`Result` 等）
  - record 自动提供不可变性、equals/hashCode/toString
  - 语法更简洁：`public record ValidationResult(boolean valid, List<String> errors) {}`
  - 比组合 `@Data + @Getter + @AllArgsConstructor` 更符合 Java 语言特性
- **序列化**：`serialVersionUID` 字段必须使用 `@Serial` 注解标记（Java 14+）
- **JavaDoc**：所有公共类和接口必须包含 `@author` 字段

## 架构模式

- **分层架构**：Controller → Service → Repository
- **数据层**：Entity/DTO/VO/BO 严格分离，使用 MapStruct 进行转换
- **认证**：无状态 JWT 双令牌，自定义安全注解与 EL 表达式
- **业务异常**：继承 `RuntimeException`
- **数据库**：使用 MyBatis Plus 作为 ORM，Spring Data 异常转换
