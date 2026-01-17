# GLM-Test 快速开发框架规格说明文档

## 项目概述

从零开始构建一个**开箱即用的快速开发框架**，目标：开发者只需关注业务逻辑开发。

### 核心决策
- **实现路径**：基础优先（先实现认证/权限/日志，再实现代码生成器）
- **生成范围**：可配置生成（按需选择生成内容，支持模板定制）
- **模板引擎**：Freemarker
- **触发方式**：Web 界面

### 技术栈
- Java 17 + Spring Boot 3.5.8 + Spring Security 6 + Maven 多模块
- MyBatis Plus 3.5.14 + PostgreSQL（含数据权限插件）
- JWT 双令牌（jjwt 0.12.6）
- BCrypt 密码加密（Spring Security 内置）
- MapStruct 1.6.3 + Lombok 1.18.38
- Passay 1.6.6（密码校验）
- Knife4j 4.4.0 + Freemarker 2.3.33

---

## 实施阶段（基础优先路径）

### 阶段 0：环境准备

**目标**：数据库初始化 + 项目配置

**任务清单**：
1. 创建 `GLM-admin/src/main/resources/application.yml` 配置文件
2. 创建主启动类 `GlmApplication.java`

**关键配置**：
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres?currentSchema=xie_tm
    username: postgres
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: none

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.xie.glm.system.domain
  configuration:
    map-underscore-to-camel-case: true

jwt:
  secret: ${JWT_SECRET}
  access-token-expiration: 1800000  # 30分钟
  refresh-token-expiration: 604800000  # 7天
```

**逻辑删除**：使用 `@TableLogic` 注解标注字段，支持不同表使用不同字段名和删除值。

**验证方式**：启动项目，访问 Knife4j 文档 `http://localhost:8080/doc.html`

---

### 阶段 1：公共基础设施

**目标**：统一响应、异常处理、工具类、密码校验

**关键文件**：

| 文件路径 | 说明 |
|---------|------|
| `GLM-common/src/main/java/com/xie/glm/common/core/Result.java` | 统一响应包装类 |
| `GLM-common/src/main/java/com/xie/glm/common/core/BaseEntity.java` | 实体基类（id/createTime/updateTime等） |
| `GLM-common/src/main/java/com/xie/glm/common/core/PageResult.java` | 分页结果包装类 |
| `GLM-common/src/main/java/com/xie/glm/common/enums/BusinessStatus.java` | 业务状态码枚举（三段式：1位系统+2位模块+2位类型） |
| `GLM-common/src/main/java/com/xie/glm/common/exception/ServiceException.java` | 业务异常类 |
| `GLM-common/src/main/java/com/xie/glm/common/util/SecurityUtil.java` | 安全工具类 |
| `GLM-common/src/main/java/com/xie/glm/common/util/PasswordValidator.java` | 密码校验工具（基于 Passay） |
| `GLM-framework/src/main/java/com/xie/glm/framework/web/GlobalExceptionHandler.java` | 全局异常处理器 |
| `GLM-framework/src/main/java/com/xie/glm/framework/config/MybatisPlusConfig.java` | MyBatis Plus 配置（含数据权限插件） |
| `GLM-framework/src/main/java/com/xie/glm/framework/config/Knife4jConfig.java` | API 文档配置 |

**密码校验规则（Passay）**：
```java
// 密码规则
- 长度：8-32 位
- 必须包含：大小写字母 + 数字 + 特殊字符
- 不能包含用户名
- 不能包含空格
- 特殊字符：!@#$%^&*()_+-=[]{}|;:,.<>?

// 密码错误提示
- 中文友好提示
- 明确指出不符合的规则
```

**统一响应格式**：
```java
public class Result<T> {
    private int code;      // 业务状态码，0表示成功
    private String message; // 响应消息
    private T data;        // 响应数据

    // 快捷方法
    public static <T> Result<T> success() { }
    public static <T> Result<T> success(T data) { }
    public static <T> Result<T> fail(String message) { }
    public static <T> Result<T> fail(int code, String message) { }
}
```

**ResponseBodyAdvice 统一处理**：
```java
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 排除已包装的 Result 类型，避免重复包装
        return !returnType.getParameterType().equals(Result.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 统一包装返回值
        if (body == null) {
            return Result.success();
        }
        return Result.success(body);
    }
}
```

**响应示例**：
```json
// 成功响应
{"code": 0, "message": "success", "data": {...}}

// 失败响应
{"code": 10101, "message": "用户不存在", "data": null}
```

---

### 阶段 2：认证授权

**目标**：Spring Security 6 + JWT 双令牌 + RBAC 权限 + BCrypt 密码加密

**权限模型**：
- **基础**：用户-角色-权限（RBAC）
- **扩展（后期）**：用户-岗位-权限，支持按岗位进行权限控制

**关键文件**：

| 文件路径 | 说明 |
|---------|------|
| `GLM-framework/src/main/java/com/xie/glm/framework/config/SecurityConfig.java` | Spring Security 6 配置 |
| `GLM-framework/src/main/java/com/xie/glm/framework/security/JwtTokenManager.java` | JWT 生成/验证/刷新 |
| `GLM-framework/src/main/java/com/xie/glm/framework/security/JwtAuthenticationFilter.java` | JWT 认证过滤器 |
| `GLM-framework/src/main/java/com/xie/glm/framework/security/JwtAuthenticationEntryPoint.java` | 未认证处理 |
| `GLM-framework/src/main/java/com/xie/glm/framework/security/CustomAccessDeniedHandler.java` | 无权限处理 |
| `GLM-framework/src/main/java/com/xie/glm/framework/security/UserDetailsServiceImpl.java` | 用户详情加载服务 |
| `GLM-framework/src/main/java/com/xie/glm/framework/security/CustomUserDetails.java` | 自定义用户详情 |
| `GLM-system/src/main/java/com/xie/glm/system/domain/SysUser.java` | 用户实体 |
| `GLM-system/src/main/java/com/xie/glm/system/domain/SysRole.java` | 角色实体 |
| `GLM-system/src/main/java/com/xie/glm/system/domain/SysMenu.java` | 菜单实体 |
| `GLM-system/src/main/java/com/xie/glm/system/domain/SysPost.java` | 岗位实体（后期） |
| `GLM-system/src/main/java/com/xie/glm/system/service/impl/UserServiceImpl.java` | 用户服务实现 |
| `GLM-system/src/main/java/com/xie/glm/system/service/impl/PermissionService.java` | 权限服务 |
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/AuthController.java` | 认证控制器 |

**Spring Security 6 核心配置**：
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    // BCrypt 密码编码器（默认强度 10）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security 6 使用 SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（使用 JWT 无需 CSRF）
            .csrf(CsrfConfigurer::disable)

            // 配置会话管理为无状态
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 配置授权规则
            .authorizeHttpRequests(auth -> auth
                // 公开接口：登录、刷新令牌
                .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()

                // Knife4j 文档
                .requestMatchers("/doc.html", "/webjars/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()

                // 健康检查
                .requestMatchers("/actuator/health").permitAll()

                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )

            // 添加 JWT 认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            // 配置异常处理
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )

            // 禁用 CORS（生产环境需要配置）
            .cors(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // 用户详情服务
    @Bean
    public UserDetailsService userDetailsService(UserDetailsServiceImpl userDetailsService) {
        return userDetailsService;
    }
}

// JWT 认证过滤器
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenManager tokenManager;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null && tokenManager.validateAccessToken(token)) {
            String username = tokenManager.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (tokenManager.validateAccessToken(token)) {
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

// 未认证处理器
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\": 401, \"message\": \"未认证或令牌已过期\"}");
    }
}

// 无权限处理器
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\": 403, \"message\": \"无权访问\"}");
    }
}
```

**BCrypt 密码加密**：
```java
// 注册用户时加密密码
@Service
public class UserServiceImpl {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void registerUser(UserRegisterDTO dto) {
        // 1. Passay 校验密码强度
        ValidationResult validation = passwordValidator.validate(dto.getPassword(), dto.getUsername());
        if (!validation.isValid()) {
            throw new ServiceException(validation.getErrors().toString());
        }

        // 2. BCrypt 加密密码
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3. 保存用户
        SysUser user = new SysUser();
        user.setUserName(dto.getUsername());
        user.setPassword(encodedPassword);  // 存储加密后的密码
        userMapper.insert(user);
    }

    // 登录时验证密码
    public UserDetails loadUserByUsername(String username) {
        SysUser user = userMapper.selectByUsername(username);

        // Spring Security 会自动使用 BCrypt 验证密码
        return CustomUserDetails.builder()
            .userId(user.getUserId())
            .username(user.getUserName())
            .password(user.getPassword())  // 存储的是 BCrypt 加密后的密码
            .roles(getRoles(user.getUserId()))
            .permissions(getPermissions(user.getUserId()))
            .build();
    }
}
```

**JWT Payload 结构**：
```json
{
  "sub": "username",
  "userId": 1,
  "roles": ["admin"],
  "posts": ["ceo", "manager"],
  "permissions": ["system:user:list"],
  "iat": 1234567890,
  "exp": 1234567890
}
```

---

### 阶段 3：核心业务模块

**目标**：用户/角色/菜单/部门管理

**关键文件**：

| 文件路径 | 说明 |
|---------|------|
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/UserController.java` | 用户 CRUD + 重置密码 |
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/RoleController.java` | 角色 CRUD + 分配权限 |
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/MenuController.java` | 菜单 CRUD + 树形结构 |
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/DeptController.java` | 部门 CRUD + 树形结构 |
| `GLM-system/src/main/java/com/xie/glm/system/service/impl/RoleServiceImpl.java` | 角色服务实现 |
| `GLM-system/src/main/java/com/xie/glm/system/service/impl/MenuServiceImpl.java` | 菜单服务实现 |

---

### 阶段 4：系统功能模块

**目标**：字典/配置/日志/定时任务

**关键文件**：

| 文件路径 | 说明 |
|---------|------|
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/DictController.java` | 字典管理 |
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/ConfigController.java` | 参数配置 |
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/LogController.java` | 日志查询 |
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/JobController.java` | 定时任务 |
| `GLM-framework/src/main/java/com/xie/glm/framework/aspect/LogAspect.java` | 日志切面 |
| `GLM-common/src/main/java/com/xie/glm/common/annotation/Log.java` | 日志注解 |
| `GLM-system/src/main/java/com/xie/glm/system/domain/SysDictType.java` | 字典类型实体 |
| `GLM-system/src/main/java/com/xie/glm/system/domain/SysOperLog.java` | 操作日志实体 |

**日志切面（混合方案）**：

方案：**默认自动记录 + @Log 注解增强**

1. **默认行为**：通过 AOP 自动记录所有 Controller 请求
   - 自动记录：URL、方法名、执行时长、操作人、IP
   - 适合大多数常规接口

2. **@Log 注解增强**：为需要详细记录的方法添加注解
   - 指定业务类型（title、businessType）
   - 控制是否保存请求/响应参数
   - 适合重要的增删改操作

3. **排除规则**：通过注解或配置排除不需要记录的接口
   - 如：健康检查、静态资源等

```java
// 不需要注解，自动记录基础信息
@GetMapping("/list")
public Result<PageResult<UserVO>> list(UserQueryDTO query) {
    // 自动记录：访问日志、执行时长
}

// 重要操作，添加注解记录详细信息
@Log(title = "用户管理", businessType = BusinessType.INSERT)
@PostMapping
public Result<Void> add(@RequestBody UserCreateDTO dto) {
    // 记录：业务类型、请求参数、响应结果
}

// 排除记录
@NoLog
@GetMapping("/health")
public Result<Void> health() {
    // 不记录日志
}
```

**@Log 注解定义**：
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String title() default "";              // 模块标题
    BusinessType businessType() default BusinessType.OTHER;
    boolean isSaveRequestData() default true;
    boolean isSaveResponseData() default true;
}
```

**日志记录内容**：
- 基础信息（所有请求）：操作人员、请求URL、执行时长、操作状态、IP地址
- 增强信息（@Log注解）：业务类型、请求参数、响应结果、异常信息

---

### 阶段 5：高级特性

**目标**：数据权限（MyBatis Plus 插件）/文件上传/导入导出

**关键文件**：

| 文件路径 | 说明 |
|---------|------|
| `GLM-common/src/main/java/com/xie/glm/common/annotation/DataScope.java` | 数据权限注解（配合 MP 插件） |
| `GLM-framework/src/main/java/com/xie/glm/framework/config/MybatisPlusConfig.java` | MyBatis Plus 配置（含 DataPermissionInterceptor） |
| `GLM-framework/src/main/java/com/xie/glm/framework/security/DataPermissionHandler.java` | 数据权限处理器（实现 MP 接口） |
| `GLM-admin/src/main/java/com/xie/glm/admin/controller/FileController.java` | 文件上传/下载 |
| `GLM-common/src/main/java/com/xie/glm/common/util/ExcelUtil.java` | Excel 工具类（基于 FastExcel） |

**数据权限范围**（MyBatis Plus 插件）：
- 1：全部数据权限
- 2：自定义数据权限
- 3：本部门数据权限
- 4：本部门及以下数据权限

**MyBatis Plus 数据权限插件配置**：
```java
// MybatisPlusConfig.java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    // 分页插件
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
    // 数据权限插件
    interceptor.addInnerInterceptor(new DataPermissionInterceptor(new DataPermissionHandler()));
    return interceptor;
}

// DataPermissionHandler.java
@Component
public class DataPermissionHandler implements IDataPermissionHandler {

    @Override
    public Expression getSqlSegment(Expression where, String tableAlias, Class<?> entityClass) {
        // 从 SecurityContext 获取当前用户
        SysUser currentUser = SecurityUtil.getCurrentUser();

        // 根据用户数据权限范围构造 SQL 条件
        int dataScope = currentUser.getDataScope();
        Long deptId = currentUser.getDeptId();

        switch (dataScope) {
            case 1: // 全部数据权限 - 不添加任何条件
                return where;
            case 2: // 自定义数据权限
                return buildCustomPermission(where, tableAlias, currentUser);
            case 3: // 本部门数据权限
                return new BinaryExpression(tableAlias + ".dept_id", "=", deptId);
            case 4: // 本部门及以下数据权限
                List<Long> deptIds = getDeptAndChildIds(deptId);
                return new InExpression(tableAlias + ".dept_id", deptIds);
            default:
                return where;
        }
    }
}

// 使用示例
@DataPermission(
    @DataColumn(alias = "u", name = "dept_id", value = "dept_id")
)
List<SysUser> selectUserList(SysUser user);
```

---

### 阶段 6：代码生成器

**目标**：Web 界面 + Freemarker 模板

**关键文件**：

| 文件路径 | 说明 |
|---------|------|
| `GLM-generator/src/main/java/com/xie/glm/service/GeneratorService.java` | 代码生成服务 |
| `GLM-generator/src/main/java/com/xie/glm/controller/GeneratorController.java` | 代码生成控制器 |
| `GLM-generator/src/main/resources/templates/entity.ftl` | Entity 模板 |
| `GLM-generator/src/main/resources/templates/mapper.ftl` | Mapper 模板 |
| `GLM-generator/src/main/resources/templates/service.ftl` | Service 接口模板 |
| `GLM-generator/src/main/resources/templates/serviceImpl.ftl` | Service 实现模板 |
| `GLM-generator/src/main/resources/templates/controller.ftl` | Controller 模板 |
| `GLM-generator/src/main/resources/templates/dto.ftl` | DTO 模板 |
| `GLM-generator/src/main/resources/templates/vo.ftl` | VO 模板 |
| `GLM-generator/src/main/resources/templates/converter.ftl` | Converter 模板 |

**API 设计**：
```
GET    /api/generator/tables        # 获取数据库表列表
GET    /api/generator/tables/{name} # 获取表详情（列信息）
POST   /api/generator/preview       # 预览生成代码
POST   /api/generator/code/download # 下载代码（zip）
POST   /api/generator/code/write    # 写入项目目录
```

**生成配置模型**：
```java
public class GeneratorConfig {
    // 表信息
    private String tableName;
    private String tableComment;

    // 生成配置
    private String className;        // 类名
    private String moduleName;       // 模块名（system）
    private String packageName;      // 包名
    private String author;           // 作者

    // 生成选项
    private boolean generateEntity;
    private boolean generateMapper;
    private boolean generateService;
    private boolean generateController;
    private boolean generateDto;
    private boolean generateVo;
    private boolean generateConverter;

    // 其他选项
    private boolean enableLombok;
    private boolean enableSwagger;
}
```

---

## 模块职责总结

### GLM-common（公共层）
- `core/`：Result（统一响应）、BaseEntity、PageResult
- `annotation/`：@DataScope（配合 MP 数据权限插件）、@Excel、@Log、@NoLog
- `converter/`：MapStruct 转换器接口（Entity ↔ DTO）
- `enums/`：BusinessStatus、ErrorCode、BusinessType
- `exception/`：ServiceException
- `util/`：SecurityUtil、StringUtil、DateUtil、ExcelUtil、PasswordValidator（基于 Passay）

### GLM-framework（框架层）
- `config/`：SecurityConfig（Spring Security 6）、MybatisPlusConfig（含分页插件、数据权限插件）、Knife4jConfig、FreemarkerConfig
- `security/`：JwtTokenManager、JwtAuthenticationFilter、JwtAuthenticationEntryPoint、CustomAccessDeniedHandler、UserDetailsServiceImpl、CustomUserDetails、DataPermissionHandler
- `web/`：GlobalExceptionHandler、ResponseAdvice
- `aspect/`：LogAspect
- `properties/`：AppProperties

### GLM-system（系统层）
- `domain/`：17 个系统实体（SysUser、SysRole、SysMenu...）
- `dto/`：服务层对外暴露的 DTO（UserDTO、RoleDTO、UserQueryDTO 等）
- `bo/`：业务对象（Service 内部多实体组合）
- `converter/`：MapStruct 转换器实现（Entity ↔ DTO）
- `mapper/`：MyBatis Mapper 接口
- `service/`：业务服务接口和实现
- `repository/`：数据访问层

### GLM-admin（管理层）
- `controller/`：REST API 控制器（Auth、User、Role、Menu...）
- `vo/`：视图对象（UserVO、RoleVO 等，用于前端展示）
- `facade/`：门面层（封装 Service 调用 + DTO → VO 转换）
- `converter/`：MapStruct 转换器（DTO ↔ VO）
- `GlmApplication.java`：主启动类

### GLM-generator（代码生成器）
- `core/`：DatabaseMetaData、CodeGenerator
- `model/`：TableInfo、ColumnInfo、GeneratorConfig
- `service/`：GeneratorService
- `controller/`：GeneratorController
- `template/`：Freemarker 模板

---

## 分层架构原则

### DTO vs VO vs BO 职责划分

| 类型 | 位置 | 职责 | 示例 | 转换方式 |
|------|------|------|------|---------|
| **Entity** | GLM-system/domain | 数据库实体映射 | SysUser、SysRole | MapStruct |
| **DTO** | GLM-system/dto | 服务层间的数据传输 | UserDTO、UserQueryDTO | MapStruct |
| **BO** | GLM-system/bo | Service 内部多实体组合 | UserCreateBO | 手动组装 |
| **VO** | GLM-admin/vo | 前端展示的数据视图 | UserVO | MapStruct |

### 对象转换策略

**使用 MapStruct 进行类型安全的对象转换**：

| 转换方向 | 位置 | 转换器 | 用途 |
|---------|------|--------|------|
| Entity → DTO | GLM-system/converter | `UserConverter` | Service 返回数据 |
| DTO → Entity | GLM-system/converter | `UserConverter` | 创建/更新实体 |
| DTO → VO | GLM-admin/converter | `UserVoConverter` | Facade 返回前端 |
| CreateDTO → BO | GLM-system/service | 手动转换 | Service 内部使用 |

**MapStruct 转换器示例**：
```java
// GLM-system/converter/UserConverter.java
@Mapper(componentModel = "spring")
public interface UserConverter {
    UserDTO toDto(SysUser entity);
    SysUser toEntity(UserDTO dto);
    List<UserDTO> toDtoList(List<SysUser> entities);
}

// GLM-admin/converter/UserVoConverter.java
@Mapper(componentModel = "spring")
public interface UserVoConverter {
    UserVO toVo(UserDTO dto);
    List<UserVO> toVoList(List<UserDTO> dtos);
}
```

### BO 使用场景

BO（Business Object）用于 Service 层内部处理**多实体组合**的业务场景，仅在 GLM-system 模块内部使用，不对外暴露。

**典型场景**：
- 创建用户时需要组合 SysUser + SysUserRole + SysUserPost
- 分配角色权限时需要组合 SysRole + SysRoleMenu
- 查询用户详情时需要组合 SysUser + SysRole + SysDept + SysPost

| 场景 | 涉及实体 | BO 用途 |
|------|---------|---------|
| 创建用户 | SysUser + SysUserRole + SysUserPost | 组合用户基本信息与关联数据 |
| 分配角色权限 | SysRole + SysRoleMenu | 组合角色与菜单权限 |
| 查询用户详情 | SysUser + SysRole + SysDept + SysPost | 组合用户完整信息 |
| 分配用户角色 | SysUser + List<SysRole> | 组合用户与角色列表 |

### Facade 层设计

**Facade 模式**：在 GLM-admin 中提供 Facade 层，作为服务调用的统一入口。

**职责**：
1. 封装对多个 Service 的调用
2. 使用 MapStruct 将 DTO 转换为 VO
3. 组装复杂的业务场景
4. 简化 Controller 的逻辑

**Facade 示例**：
```java
// GLM-admin/facade/UserFacade.java
@Service
public class UserFacade {

    private final IUserService userService;
    private final IRoleService roleService;
    private final IDeptService deptService;
    private final UserVoConverter voConverter;

    public PageResult<UserVO> listUsers(UserQueryDTO query) {
        // 调用 Service 获取 DTO
        PageResult<UserDTO> dtoPage = userService.listUsers(query);

        // 使用 MapStruct 转换为 VO
        List<UserVO> voList = voConverter.toVoList(dtoPage.getRecords());

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    public UserVO getUserDetail(Long userId) {
        // 调用多个 Service 组装数据
        UserDTO userDto = userService.getUserById(userId);
        List<RoleDTO> roles = roleService.getRolesByUserId(userId);
        DeptDTO dept = deptService.getDeptById(userDto.getDeptId());

        // 组装 VO（可能需要额外处理）
        UserVO vo = voConverter.toVo(userDto);
        vo.setRoleNames(roles.stream().map(RoleDTO::getRoleName).toList());
        vo.setDeptName(dept.getDeptName());

        return vo;
    }
}
```

### 数据流向（包含 Facade 和 MapStruct）

```
前端请求
    ↓
Controller (GLM-admin)
    ↓ 接收 VO/QueryDTO
Facade (GLM-admin/facade)
    ↓ 调用 Service 组装业务逻辑
Service (GLM-system)
    ↓ 内部使用 BO 组合多实体
    ↓ Entity → DTO (MapStruct)
    ↓ 返回 DTO
Facade (GLM-admin/facade)
    ↓ DTO → VO (MapStruct)
    ↓ 组装展示数据
Controller (GLM-admin)
    ↓ 返回 VO
前端响应
```

---

## 验证测试

### 阶段 0 验证
```bash
# 启动项目
mvn spring-boot:run -pl GLM-admin

# 访问 Knife4j 文档
curl http://localhost:8080/doc.html
```

### 阶段 2 验证（认证）
```bash
# 登录获取 Token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 使用 Token 访问受保护资源
curl http://localhost:8080/api/system/users \
  -H "Authorization: Bearer <access_token>"
```

### 阶段 6 验证（代码生成器）
```bash
# 获取数据库表列表
curl http://localhost:8080/api/generator/tables

# 预览生成代码
curl -X POST http://localhost:8080/api/generator/preview \
  -H "Content-Type: application/json" \
  -d '{"tableName":"sys_user","generateEntity":true,...}'
```

### 全量测试
```bash
# 运行所有测试
mvn test

# 运行特定模块测试
mvn test -pl GLM-system
mvn test -pl GLM-admin
```

---

## 新增功能分期规划

### 分期原则
- **阶段 0-2**：基础设施必备项
- **阶段 3**：核心业务支撑项
- **阶段 4**：系统功能增强项
- **阶段 5**：高级特性项
- **阶段 6+**：运维和长期优化项

### 分期详细规划

| 功能 | 优先级 | 加入阶段 | 说明 |
|------|-------|---------|------|
| **参数校验** | 高 | 阶段 1 | Jakarta Validation，所有接口必备 |
| **CORS 配置** | 高 | 阶段 0 | 跨域访问，前后端分离必备 |
| **环境配置** | 高 | 阶段 0 | dev/test/prod 多环境 |
| **健康检查** | 高 | 阶段 0 | Spring Actuator 基础监控 |
| **日志配置** | 高 | 阶段 0 | Logback 文件滚动配置 |
| **乐观锁** | 中 | 阶段 3 | @Version 字段，防止并发冲突 |
| **缓存机制** | 中 | 阶段 3 | Spring Cache + Caffeine（权限/字典缓存） |
| **审计日志** | 中 | 阶段 3 | 数据变更记录（@CreatedBy/@LastModifiedDate） |
| **在线用户管理** | 中 | 阶段 4 | 踢出用户、强制下线 |
| **登录限流** | 中 | 阶段 4 | 防止暴力破解（Redis 计数器） |
| **密码重置** | 中 | 阶段 4 | 忘记密码流程 |
| **定时任务管理** | 中 | 阶段 4 | 动态添加/暂停任务 |
| **文件存储策略** | 低 | 阶段 5 | 本地/OSS/MinIO 抽象 |
| **敏感配置加密** | 低 | 阶段 5 | Jasypt 加密配置 |
| **异步任务** | 低 | 阶段 5 | @Async + 线程池 |
| **API 版本控制** | 低 | 阶段 7+ | /api/v1/、/api/v2/ |
| **限流/熔断** | 低 | 阶段 7+ | Resilience4j |
| **链路追踪** | 低 | 阶段 7+ | Micrometer Tracing |
| **容器化** | 低 | 阶段 7+ | Docker/DockerCompose |
| **TestContainers** | 低 | 阶段 7+ | 集成测试真实 DB |
| **测试覆盖率** | 低 | 阶段 7+ | JaCoCo |

### 阶段时间估算（更新后）

| 阶段 | 时间 | 新增内容 |
|------|------|---------|
| 阶段 0：环境准备 | 1-2 天 | +环境配置、CORS、日志配置、健康检查 |
| 阶段 1：公共基础设施 | 2-3 天 | +参数校验 |
| 阶段 2：认证授权 | 5-7 天 | - |
| 阶段 3：核心业务模块 | 8-12 天 | +乐观锁、缓存、审计日志 |
| 阶段 4：系统功能模块 | 7-10 天 | +在线用户、登录限流、密码重置、任务管理 |
| 阶段 5：高级特性 | 6-9 天 | +文件存储、配置加密、异步任务 |
| 阶段 6：代码生成器 | 7-10 天 | - |
| 阶段 7+：运维优化 | 待定 | API 版本、限流熔断、链路追踪、容器化 |
| **总计** | **36-53 天 + 运维优化** | |

---

## 关键原则（不可妥协）

1. **TDD 优先**：所有功能先写失败的测试
2. **参数化测试**：使用 `@ParameterizedTest` + `@MethodSource`
3. **集成测试为主**：使用 `@SpringBootTest`
4. **统一响应格式**：`{"code": 0, "message": "success", "data": {}}`
5. **异常链**：`throw new ServiceException("message", cause)`
6. **构造器注入**：优先使用构造器注入
7. **RESTful 规范**：GET 查询、POST 创建、PUT 更新、DELETE 删除
