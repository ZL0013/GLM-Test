# GLM-Test 快速开发框架 - 核心功能技术实现方案

**文档版本**: 1.0
**创建日期**: 2026-01-16
**规格文档**: `spec.md`
**宪法文档**: `constitution.md`

---

## 1. 技术上下文总结

### 1.1 核心技术栈

| 技术类别 | 选型 | 版本 | 用途说明 |
|---------|------|------|---------|
| **语言** | Java | 17+ | 核心开发语言 |
| **框架** | Spring Boot + Security | 3.5.8 + 6.x | 应用框架 + 安全认证 |
| **构建** | Maven | - | 多模块项目构建与依赖管理 |
| **数据库** | PostgreSQL | - | 关系型数据存储 |
| **ORM** | MyBatis Plus | 3.5.14 | 持久层框架（含分页、数据权限插件） |
| **认证** | jjwt | 0.12.6 | JWT 双令牌机制 |
| **密码加密** | BCrypt | Spring Security 内置 | 密码单向加密 |
| **密码校验** | Passay | 1.6.6 | 密码强度校验 |
| **代码生成** | MapStruct + Lombok | 1.6.3 + 1.18.38 | 对象映射 + 代码简化 |
| **API文档** | Knife4j | 4.4.0 | Swagger UI 增强 |
| **模板引擎** | Freemarker | 2.3.33 | 代码生成模板 |

### 1.2 架构风格

- **分层架构**: Controller → Service → Repository，严格职责分离
- **RESTful Level 2-3**: 统一响应格式，HTTP 状态码固定 200，业务状态通过响应体判断
- **无状态认证**: JWT 双令牌，服务端不存储会话
- **AOP 增强**: 日志切面、数据权限切面

---

## 2. "合宪性"审查

本方案必须逐条对照 `constitution.md` 严格进行合规性审查：

### 2.1 第一条：简单性原则 ✓

| 宪法条款 | 合规性说明 | 实现措施 |
|---------|-----------|---------|
| 1.1 YAGNI | ✓ 只实现 spec.md 明确要求的功能 | 严格按阶段实施，不提前实现未要求功能 |
| 1.2 标准库优先 | ✓ 优先使用 Java SE + Spring Framework 核心 | 避免引入额外第三方库，仅在必要时使用 jjwt、mybatis-plus、knife4j |
| 1.3 反过度工程 | ✓ 简单 POJO 优于复杂抽象 | Result 类为简单包装，不引入复杂响应抽象层次 |
| **编码规范** | ✓ 使用 Lombok 消除样板代码 | 所有实体使用 `@Data`，`serialVersionUID` 使用 `@Serial` 注解，JavaDoc 包含 `@author` |

### 2.2 第二条：测试先行铁律 ✓

| 宪法条款 | 合规性说明 | 实现措施 |
|---------|-----------|---------|
| 2.1 TDD 循环 | ✓ 严格遵循 Red-Green-Refactor | 每个功能从编写失败测试开始 |
| 2.2 参数化测试 | ✓ 使用 @ParameterizedTest + @MethodSource | 测试数据与测试逻辑分离 |
| 2.3 集成测试优先 | ✓ 使用 @SpringBootTest 进行真实环境测试 | 优先集成测试，必要时使用 Mockito |

### 2.3 第三条：明确性原则 ✓

| 宪法条款 | 合规性说明 | 实现措施 |
|---------|-----------|---------|
| 3.1 异常处理 | ✓ 异常链传递 | 所有异常使用 `throw new ServiceException("message", cause)` |
| 3.2 依赖注入 | ✓ 构造器注入优先 | 所有依赖通过 Spring 容器管理，禁止静态变量传状态 |

### 2.4 第四条：Spring约定 ✓

| 宪法条款 | 合规性说明 | 实现措施 |
|---------|-----------|---------|
| 4.1 自动配置 | ✓ 遵循 Spring Boot 约定 | 最小化自定义配置 |
| 4.2 配置绑定 | ✓ 使用 @ConfigurationProperties | JWT、应用配置使用类型安全绑定 |
| 4.3 分层清晰 | ✓ Controller-Service-Repository | 严格职责分离 |
| 4.4 全局响应包装 | ✓ 统一 Result 格式 | ResponseBodyAdvice 自动包装，code 为 0 表示成功 |
| 4.5 RESTful 规范 | ✓ GET/POST/PUT/DELETE | URL 使用名词复数，禁止动词 |

### 2.5 第五条：异常规范 ✓

| 宪法条款 | 合规性说明 | 实现措施 |
|---------|-----------|---------|
| 5.1 自定义异常 | ✓ 继承 RuntimeException | ServiceException 为业务异常基类 |
| 5.2 全局异常处理 | ✓ @ControllerAdvice + @ExceptionHandler | GlobalExceptionHandler 统一处理 |
| 5.3 数据访问异常 | ✓ Spring Data 异常转换 | MyBatis Plus 集成 Spring 异常转换 |

### 2.6 第六条：认证授权 ✓

| 宪法条款 | 合规性说明 | 实现措施 |
|---------|-----------|---------|
| 6.1 JWT 双 token | ✓ Access Token + Refresh Token | 访问令牌 30 分钟，刷新令牌 7 天 |
| 6.2 权限校验 | ✓ 自定义注解 + EL 表达式 | @PreAuthorize("hasAuthority('xxx')") |
| 6.3 无状态 | ✓ 服务端不存储会话 | 用户状态编码在 JWT 中 |

**审查结论**: 本技术方案完全符合 `constitution.md` 所有条款要求。

---

## 3. 项目结构细化

### 3.1 模块依赖关系

```
GLM-admin (启动入口)
    ↓ 依赖
GLM-system (业务层)
    ↓ 依赖
GLM-framework + GLM-common (基础层)
```

```
GLM-generator (独立模块，可选)
    ↓ 依赖
GLM-framework + GLM-common
```

### 3.2 包结构详细设计

#### GLM-common (公共层)
```
com.xie.glm.common/
├── annotation/          # 自定义注解
│   ├── DataScope.java   # 数据权限注解（配合 MP 插件）
│   ├── Log.java         # 日志注解
│   ├── Excel.java       # Excel 导入导出注解
│   └── NoLog.java       # 排除日志注解
├── core/                # 核心类
│   ├── Result.java      # 统一响应包装
│   ├── BaseEntity.java  # 实体基类
│   └── PageResult.java # 分页结果
├── converter/           # MapStruct 转换器基础接口
│   └── EntityConverter.java  # Entity ↔ DTO 基础转换器
├── enums/               # 枚举类
│   ├── BusinessStatus.java    # 业务状态码
│   ├── BusinessType.java      # 业务操作类型
│   └── ErrorCode.java         # 错误码枚举
├── exception/           # 异常类
│   ├── ServiceException.java  # 业务异常
│   └── {module}Exception.java # 模块异常
└── util/                # 工具类
    ├── SecurityUtil.java      # 安全工具
    ├── PasswordValidator.java # 密码校验工具（Passay）
    ├── StringUtil.java       # 字符串工具
    ├── DateUtil.java         # 日期工具
    └── ExcelUtil.java        # Excel 工具
```

#### GLM-framework (框架层)
```
com.xie.glm.framework/
├── config/              # 配置类
│   ├── SecurityConfig.java        # Spring Security 6 配置
│   ├── MybatisPlusConfig.java     # MyBatis Plus 配置（分页+数据权限插件）
│   ├── Knife4jConfig.java         # API 文档配置
│   ├── FreemarkerConfig.java      # 模板引擎配置
│   └── JwtConfig.java             # JWT 配置属性
├── security/            # 安全认证
│   ├── JwtTokenManager.java       # JWT 管理器
│   ├── JwtAuthenticationFilter.java # JWT 认证过滤器
│   ├── JwtAuthenticationEntryPoint.java # 未认证处理器
│   ├── CustomAccessDeniedHandler.java # 无权限处理器
│   ├── UserDetailsServiceImpl.java # 用户详情加载服务
│   ├── CustomUserDetails.java     # 自定义用户详情
│   ├── DataPermissionHandler.java # 数据权限处理器（MP 接口实现）
│   └── SecurityUtils.java         # 安全工具类
├── web/                 # Web 层
│   ├── GlobalExceptionHandler.java # 全局异常处理
│   └── ResponseAdvice.java        # 响应包装处理
├── aspect/              # 切面
│   └── LogAspect.java            # 日志切面
└── properties/          # 配置属性
    └── AppProperties.java         # 应用配置属性
```

#### GLM-system (系统层)
```
com.xie.glm.system/
├── domain/              # 实体类 (17个系统实体)
│   ├── SysUser.java     # 用户实体
│   ├── SysRole.java     # 角色实体
│   ├── SysMenu.java     # 菜单实体
│   ├── SysDept.java     # 部门实体
│   ├── SysPost.java     # 岗位实体
│   ├── SysDictType.java # 字典类型
│   ├── SysDictData.java # 字典数据
│   ├── SysConfig.java   # 参数配置
│   ├── SysNotice.java   # 通知公告
│   ├── SysOperLog.java  # 操作日志
│   ├── SysLoginInfo.java # 登录日志
│   ├── SysJob.java      # 定时任务
│   ├── SysJobLog.java   # 任务日志
│   ├── SysUserPost.java # 用户岗位关联
│   ├── SysUserRole.java # 用户角色关联
│   └── SysRoleMenu.java # 角色菜单关联
├── dto/                 # 服务层对外暴露的 DTO
│   ├── UserDTO.java            # 用户 DTO
│   ├── RoleDTO.java            # 角色 DTO
│   ├── MenuDTO.java            # 菜单 DTO
│   ├── DeptDTO.java            # 部门 DTO
│   └── query/                 # 查询条件 DTO
│       ├── UserQueryDTO.java
│       ├── RoleQueryDTO.java
│       └── ...
├── bo/                  # 业务对象（Service 内部多实体组合）
│   ├── UserCreateBO.java       # 创建用户 BO（User + UserRole + UserPost）
│   ├── UserRoleAssignBO.java   # 用户角色分配 BO
│   ├── RoleMenuAssignBO.java   # 角色菜单权限分配 BO
│   ├── UserDetailBO.java       # 用户详情 BO（User + Role + Dept + Post）
│   └── ...
├── converter/           # MapStruct 转换器实现（Entity ↔ DTO）
│   ├── UserConverter.java      # 用户转换器
│   ├── RoleConverter.java      # 角色转换器
│   ├── MenuConverter.java      # 菜单转换器
│   └── ...
├── mapper/              # MyBatis Mapper 接口
│   ├── SysUserMapper.java
│   ├── SysRoleMapper.java
│   └── ...
├── service/             # 业务服务
│   ├── IUserService.java        # 用户服务接口
│   ├── IRoleService.java        # 角色服务接口
│   ├── IMenuService.java        # 菜单服务接口
│   ├── IPermissionService.java  # 权限服务接口
│   └── impl/
│       ├── UserServiceImpl.java
│       ├── RoleServiceImpl.java
│       └── ...
└── repository/          # 数据访问层 (可选)
    └── UserRepository.java
```

#### GLM-admin (管理层)
```
com.xie.glm.admin/
├── controller/          # REST API 控制器
│   ├── AuthController.java       # 认证控制器
│   ├── UserController.java        # 用户管理
│   ├── RoleController.java        # 角色管理
│   ├── MenuController.java        # 菜单管理
│   ├── DeptController.java        # 部门管理
│   ├── PostController.java        # 岗位管理
│   ├── DictController.java        # 字典管理
│   ├── ConfigController.java      # 参数配置
│   ├── LogController.java         # 日志查询
│   ├── JobController.java         # 定时任务
│   ├── NoticeController.java      # 通知公告
│   └── FileController.java        # 文件管理
├── vo/                  # 视图对象（用于前端展示）
│   ├── UserVO.java             # 用户 VO
│   ├── RoleVO.java             # 角色 VO
│   ├── MenuVO.java             # 菜单 VO
│   └── ...
├── facade/              # 门面层（封装 Service 调用 + DTO → VO 转换）
│   ├── UserFacade.java         # 用户门面
│   ├── RoleFacade.java         # 角色门面
│   ├── MenuFacade.java         # 菜单门面
│   └── ...
├── converter/           # MapStruct 转换器（DTO ↔ VO）
│   ├── UserVoConverter.java    # 用户 VO 转换器
│   ├── RoleVoConverter.java    # 角色 VO 转换器
│   ├── MenuVoConverter.java    # 菜单 VO 转换器
│   └── ...
└── GlmApplication.java  # 主启动类
```

#### GLM-generator (代码生成器)
```
com.xie.glm.generator/
├── core/                # 核心组件
│   ├── DatabaseMetaData.java     # 数据库元数据读取
│   └── CodeGenerator.java        # 代码生成引擎
├── model/               # 模型类
│   ├── TableInfo.java            # 表信息
│   ├── ColumnInfo.java           # 列信息
│   └── GeneratorConfig.java      # 生成配置
├── service/             # 服务
│   └── GeneratorService.java     # 生成服务
├── controller/          # 控制器
│   └── GeneratorController.java  # 生成控制器
└── resources/templates/ # Freemarker 模板
    ├── entity.ftl       # Entity 模板
    ├── mapper.ftl       # Mapper 模板
    ├── service.ftl      # Service 接口模板
    ├── serviceImpl.ftl  # Service 实现模板
    ├── controller.ftl   # Controller 模板
    ├── dto.ftl          # DTO 模板
    ├── vo.ftl           # VO 模板
    └── converter.ftl    # Converter 模板
```

### 3.3 关键类设计

#### Result<T> 统一响应包装

**编码规范**：
- 使用 `@Data` 注解消除 getter/setter/toString 样板代码
- 使用 `@Serial` 注解标记 `serialVersionUID`（Java 14+）
- JavaDoc 必须包含 `@author` 字段（阿里巴巴规范）

```java
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应包装类
 *
 * <p>所有 Controller 接口必须使用此类的实例作为返回值。
 *
 * @author xie
 */
@Data
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int code;       // 0=成功, 非0=失败
    private String message;  // 响应消息
    private T data;         // 响应数据

    public static <T> Result<T> success() { ... }
    public static <T> Result<T> success(T data) { ... }
    public static <T> Result<T> fail(String message) { ... }
    public static <T> Result<T> fail(int code, String message) { ... }
}
```

#### 业务状态码设计
```java
public enum BusinessStatus {
    SUCCESS(0, "成功"),
    ERROR(1, "失败"),

    // 系统模块 (10xxx)
    SYSTEM_ERROR(10000, "系统异常"),

    // 用户模块 (11xxx)
    USER_NOT_FOUND(11001, "用户不存在"),
    USER_PASSWORD_ERROR(11002, "密码错误"),
    USER_ACCOUNT_DISABLED(11003, "账号已禁用"),

    // 角色模块 (12xxx)
    ROLE_NOT_FOUND(12001, "角色不存在"),
    ROLE_NAME_DUPLICATE(12002, "角色名称已存在"),

    // 菜单模块 (13xxx)
    MENU_NOT_FOUND(13001, "菜单不存在"),
    MENU_HAS_CHILD(13002, "菜单存在子菜单，不允许删除"),

    // 部门模块 (14xxx)
    DEPT_NOT_FOUND(14001, "部门不存在"),
    DEPT_HAS_CHILD(14002, "部门存在子部门，不允许删除"),
    DEPT_HAS_USER(14003, "部门存在用户，不允许删除"),

    // ... 其他模块
    ;
}
```

#### 密码校验设计（Passay）

**密码规则**：
- 长度：8-32 位
- 必须包含：大小写字母 + 数字 + 特殊字符
- 不能包含用户名
- 不能包含空格
- 特殊字符：!@#$%^&*()_+-=[]{}|;:,.<>?

**PasswordValidator 实现**：
```java
// GLM-common/util/PasswordValidator.java
@Component
public class PasswordValidator {

    private final org.passay.PasswordValidator passayValidator;

    public PasswordValidator() {
        this.passayValidator = new org.passay.PasswordValidator(
            // 长度规则
            new LengthRule(8, 32),

            // 字符规则
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            new CharacterRule(EnglishCharacterData.Digit, 1),
            new CharacterRule(EnglishCharacterData.Special, 1),

            // 不允许包含用户名
            new UsernameRule(),

            // 不允许包含空格
            new WhitespaceRule()
        );
    }

    public ValidationResult validate(String password, String username) {
        RuleResult result = passayValidator.validate(new PasswordData(password, username));

        if (result.isValid()) {
            return ValidationResult.success();
        }

        // 中文友好提示
        List<String> messages = passayValidator.getMessages(result)
            .stream()
            .map(this::toChineseMessage)
            .collect(Collectors.toList());

        return ValidationResult.fail(messages);
    }

    private String toChineseMessage(String message) {
        if (message.contains("uppercase")) return "密码必须包含大写字母";
        if (message.contains("lowercase")) return "密码必须包含小写字母";
        if (message.contains("digit")) return "密码必须包含数字";
        if (message.contains("special")) return "密码必须包含特殊字符";
        if (message.contains("whitespace")) return "密码不能包含空格";
        if (message.contains("username")) return "密码不能包含用户名";
        if (message.contains("length")) return "密码长度必须在8-32位之间";
        return message;
    }
}
```

### 3.4 分层架构原则：DTO vs VO vs BO 职责划分

**核心原则**：严格遵循分层架构，Entity、DTO、BO、VO 各司其职，避免模块间耦合。使用 MapStruct 进行类型安全的对象转换，使用 Facade 模式封装服务调用。

| 类型 | 位置 | 职责 | 使用场景 | 转换方式 |
|------|------|------|---------|---------|
| **Entity** | GLM-system/domain | 数据库实体映射 | Service 层内部使用 | MapStruct |
| **DTO** | GLM-system/dto | 服务层间的数据传输 | Service 返回给 Facade 的数据 | MapStruct |
| **QueryDTO** | GLM-system/dto/query | 查询条件 DTO | Facade 传递给 Service 的查询参数 | - |
| **BO** | GLM-system/bo | Service 内部多实体组合 | RBAC 场景中的多实体组合操作 | 手动组装 |
| **VO** | GLM-admin/vo | 前端展示的数据视图 | Facade 返回给 Controller 的数据 | MapStruct |

#### 对象转换策略（MapStruct）

**使用 MapStruct 1.6.3 进行类型安全的对象转换**：

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

#### BO 使用场景说明

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

#### BO 代码示例

```java
// GLM-system/bo/UserCreateBO.java
/**
 * 创建用户业务对象
 * 用于组合用户基本信息与角色、岗位关联数据
 */
public class UserCreateBO {
    // 用户基本信息
    private String userName;
    private String password;
    private String email;
    private Long deptId;

    // 关联角色ID列表
    private List<Long> roleIds;

    // 关联岗位ID列表
    private List<Long> postIds;

    // getters/setters
}

// GLM-system/bo/RoleMenuAssignBO.java
/**
 * 角色菜单权限分配业务对象
 */
public class RoleMenuAssignBO {
    // 角色ID
    private Long roleId;

    // 菜单权限ID列表
    private List<Long> menuIds;

    // 是否全选
    private boolean menuCheckStrictly;

    // getters/setters
}

// GLM-system/bo/UserDetailBO.java
/**
 * 用户详情业务对象
 * 组合用户、角色、部门、岗位完整信息
 */
public class UserDetailBO {
    // 用户基本信息
    private SysUser user;

    // 用户角色列表
    private List<SysRole> roles;

    // 用户所属部门
    private SysDept dept;

    // 用户岗位列表
    private List<SysPost> posts;

    // getters/setters
}
```

#### Facade 层设计

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

    public void createUser(UserCreateDTO dto) {
        userService.createUser(dto);
    }
}
```

#### Controller 使用 Facade

```java
// GLM-admin/controller/UserController.java
@RestController
@RequestMapping("/api/system/users")
@Tag(name = "用户管理")
public class UserController {

    private final UserFacade userFacade;

    @GetMapping
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<PageResult<UserVO>> list(UserQueryDTO query) {
        return Result.success(userFacade.listUsers(query));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<UserVO> getDetail(@PathVariable Long id) {
        return Result.success(userFacade.getUserDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Void> create(@Valid @RequestBody UserCreateDTO dto) {
        userFacade.createUser(dto);
        return Result.success();
    }
}
```

#### 数据流向（包含 Facade 和 MapStruct）

```
前端请求
    ↓
Controller (GLM-admin)
    ↓ 接收 QueryDTO
Facade (GLM-admin/facade)
    ↓ 调用 Service 组装业务逻辑
Service (GLM-system)
    ↓ 内部使用 BO 组合多实体
    ↓ Entity → DTO (MapStruct: UserConverter)
    ↓ 返回 DTO
Facade (GLM-admin/facade)
    ↓ DTO → VO (MapStruct: UserVoConverter)
    ↓ 组装展示数据
Controller (GLM-admin)
    ↓ 返回 VO
前端响应
```

**示例代码**（使用 MapStruct 和 Facade）：

```java
// GLM-system/dto/UserDTO.java (服务提供方)
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    private Long deptId;
    // ... 服务层需要传递的字段
}

// GLM-system/dto/query/UserQueryDTO.java
public class UserQueryDTO extends PageQuery {
    private String userName;
    private Long deptId;
    private String status;
    // ... 查询条件
}

// GLM-admin/vo/UserVO.java (服务消费方)
public class UserVO {
    private Long userId;
    private String userName;
    private String email;
    private String deptName;  // 展示用，可能来自关联查询
    private List<String> roleNames;  // 角色名称列表
    private String statusText;
    // ... 前端展示需要的字段
}

// GLM-system/converter/UserConverter.java (Entity ↔ DTO)
@Mapper(componentModel = "spring")
public interface UserConverter {
    UserDTO toDto(SysUser entity);
    SysUser toEntity(UserDTO dto);
    List<UserDTO> toDtoList(List<SysUser> entities);
}

// GLM-admin/converter/UserVoConverter.java (DTO ↔ VO)
@Mapper(componentModel = "spring")
public interface UserVoConverter {
    UserVO toVo(UserDTO dto);
    List<UserVO> toVoList(List<UserDTO> dtos);
}

// GLM-system/service/impl/UserServiceImpl.java (使用 BO 和 MapStruct)
@Service
public class UserServiceImpl implements IUserService {

    private final UserConverter userConverter;

    // 创建用户：使用 BO 组合多个实体
    @Transactional
    public void createUser(UserCreateDTO dto) {
        // 1. 将 DTO 转换为 BO（Service 内部使用）
        UserCreateBO bo = convertToBO(dto);

        // 2. 使用 MapStruct 转换为 Entity 并保存用户基本信息
        SysUser user = new SysUser();
        user.setUserName(bo.getUserName());
        user.setPassword(bo.getPassword());
        userMapper.insert(user);

        // 3. 保存用户角色关联（从 BO 获取）
        if (CollectionUtils.isNotEmpty(bo.getRoleIds())) {
            bo.getRoleIds().forEach(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            });
        }

        // 4. 保存用户岗位关联（从 BO 获取）
        if (CollectionUtils.isNotEmpty(bo.getPostIds())) {
            bo.getPostIds().forEach(postId -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(user.getUserId());
                userPost.setPostId(postId);
                userPostMapper.insert(userPost);
            });
        }
    }

    // 查询用户列表：使用 MapStruct 转换
    public PageResult<UserDTO> listUsers(UserQueryDTO query) {
        Page<SysUser> page = userMapper.selectPage(buildPage(query), buildWrapper(query));
        List<UserDTO> dtoList = userConverter.toDtoList(page.getRecords());
        return new PageResult<>(dtoList, page.getTotal());
    }

    // 查询用户详情：使用 BO 组合多个实体
    public UserDTO getUserDetail(Long userId) {
        // 1. 查询用户基本信息
        SysUser user = userMapper.selectById(userId);

        // 2. 使用 MapStruct 转换为 DTO
        UserDTO dto = userConverter.toDto(user);

        // 3. DTO 可以包含额外需要传递给 Facade 的数据
        return dto;
    }

    private UserCreateBO convertToBO(UserCreateDTO dto) {
        UserCreateBO bo = new UserCreateBO();
        bo.setUserName(dto.getUserName());
        bo.setPassword(dto.getPassword());
        bo.setEmail(dto.getEmail());
        bo.setDeptId(dto.getDeptId());
        bo.setRoleIds(dto.getRoleIds());
        bo.setPostIds(dto.getPostIds());
        return bo;
    }
}
```

---

### 3.5 Spring Security 6 + BCrypt 认证架构

**核心原则**：严格使用 Spring Security 6 特性，BCrypt 密码加密，无状态 JWT 认证。

#### Spring Security 6 核心配置

```java
// GLM-framework/config/SecurityConfig.java
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

    // Spring Security 6 使用 SecurityFilterChain（移除了 WebSecurityConfigurerAdapter）
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（使用 JWT 无需 CSRF）
            .csrf(CsrfConfigurer::disable)

            // 配置会话管理为无状态
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 配置授权规则（Spring Security 6 新风格）
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

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

// 未认证处理器（401）
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

// 无权限处理器（403）
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

#### BCrypt 密码加密

```java
// 注册/修改密码时加密
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

        // 2. BCrypt 加密密码（每次加密结果不同，但验证一致）
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3. 保存用户
        SysUser user = new SysUser();
        user.setUserName(dto.getUsername());
        user.setPassword(encodedPassword);  // 存储加密后的密码
        userMapper.insert(user);
    }

    // 登录时 Spring Security 自动验证密码
}

// BCrypt 加密特点：
// 1. 每次加密同一个明文密码，结果都不同（加盐）
// 2. 加密格式：$2a$10$...
// 3. 不可逆，只能通过 matches() 方法验证
// 4. 默认强度 10（4-31），越高越安全但越慢
```

#### Controller 权限注解

```java
// Spring Security 6 方法级权限注解
@RestController
@RequestMapping("/api/system/users")
@Tag(name = "用户管理")
public class UserController {

    // 使用 @PreAuthorize 注解（需要开启 @EnableMethodSecurity）
    @GetMapping
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<PageResult<UserVO>> list(UserQueryDTO query) {
        return Result.success(userFacade.listUsers(query));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<UserVO> getDetail(@PathVariable Long id) {
        return Result.success(userFacade.getUserDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Void> create(@Valid @RequestBody UserCreateDTO dto) {
        userFacade.createUser(dto);
        return Result.success();
    }
}
```

#### Spring Security 6 主要变化

| 特性 | Spring Security 5 | Spring Security 6 |
|------|------------------|------------------|
| 配置方式 | WebSecurityConfigurerAdapter | SecurityFilterChain Bean |
| 授权配置 | authorizeRequests() | authorizeHttpRequests() |
| 匹配器 | antMatchers() | requestMatchers() |
| 方法级安全 | @EnableGlobalMethodSecurity | @EnableMethodSecurity |
| Lambda DSL | 可选 | 推荐（函数式风格） |

---

## 4. 详细实施计划

### 4.1 阶段 0：环境准备

**目标**: 建立可运行的项目骨架

#### 任务列表

| 序号 | 任务 | 输出物 | 验证方式 |
|------|------|--------|---------|
| 0.1 | 创建 application.yml 配置文件 | `GLM-admin/src/main/resources/application.yml` | 配置文件存在且格式正确 |
| 0.2 | 创建主启动类 | `GLM-admin/.../GlmApplication.java` | 可启动项目 |
| 0.3 | 配置 Maven 依赖 | 各模块 `pom.xml` | `mvn clean install` 成功 |

#### 关键配置
```yaml
spring:
  application:
    name: glm-admin
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres?currentSchema=xie_tm
    username: postgres
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.xie.glm.system.domain
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl

jwt:
  secret: ${JWT_SECRET:glm-test-secret-key-for-jwt-token-generation-please-change-in-production}
  access-token-expiration: 1800000   # 30分钟
  refresh-token-expiration: 604800000  # 7天

logging:
  level:
    com.xie.glm: debug
    org.springframework.security: debug
```

#### 验证测试
```bash
# 构建项目
mvn clean install

# 启动项目
mvn spring-boot:run -pl GLM-admin

# 访问 Knife4j 文档
curl http://localhost:8080/doc.html
```

---

### 4.2 阶段 1：公共基础设施

**目标**: 建立统一响应、异常处理、工具类基础

#### TDD 测试先行

**测试类**: `ResultTest.java`
```java
@ParameterizedTest
@MethodSource("provideSuccessData")
void testSuccessResponse(Object data, String expectedMessage) {
    Result<Object> result = Result.success(data);
    assertEquals(0, result.getCode());
    assertEquals(expectedMessage, result.getMessage());
    assertEquals(data, result.getData());
}

private static Stream<Arguments> provideSuccessData() {
    return Stream.of(
        Arguments.of(null, "success"),
        Arguments.of("test data", "success"),
        Arguments.of(123, "success")
    );
}
```

#### 关键文件清单

| 文件 | 职责 | TDD 测试 |
|------|------|---------|
| `Result.java` | 统一响应包装 | `ResultTest.java` |
| `BaseEntity.java` | 实体基类 | `BaseEntityTest.java` |
| `PageResult.java` | 分页结果 | `PageResultTest.java` |
| `BusinessStatus.java` | 业务状态码 | `BusinessStatusTest.java` |
| `ServiceException.java` | 业务异常 | `ServiceExceptionTest.java` |
| `GlobalExceptionHandler.java` | 全局异常处理 | `GlobalExceptionHandlerTest.java` |
| `ResponseAdvice.java` | 响应包装处理 | `ResponseAdviceTest.java` |

---

### 4.3 阶段 2：认证授权

**目标**: JWT 双令牌 + RBAC 权限

#### TDD 测试先行

**测试类**: `JwtTokenManagerTest.java`
```java
@ParameterizedTest
@CsvSource({
    "admin, 30, true",
    "user, 60, true",
    "test, -1, false"
})
void testGenerateAccessToken(String username, long minutes, boolean shouldSucceed) {
    if (!shouldSucceed) {
        assertThrows(IllegalArgumentException.class, () ->
            jwtTokenManager.generateAccessToken(username));
    } else {
        String token = jwtTokenManager.generateAccessToken(username);
        assertNotNull(token);
        assertEquals(username, jwtTokenManager.extractUsername(token));
    }
}
```

#### 关键文件清单

| 文件 | 职责 | TDD 测试 |
|------|------|---------|
| `JwtTokenManager.java` | JWT 生成/验证/刷新 | `JwtTokenManagerTest.java` |
| `JwtAuthenticationFilter.java` | JWT 过滤器 | `JwtAuthenticationFilterTest.java` |
| `UserDetailsServiceImpl.java` | 用户详情加载 | `UserDetailsServiceTest.java` |
| `SysUser.java` | 用户实体 | `SysUserTest.java` |
| `SysRole.java` | 角色实体 | `SysRoleTest.java` |
| `SysMenu.java` | 菜单实体 | `SysMenuTest.java` |
| `UserServiceImpl.java` | 用户服务实现 | `UserServiceTest.java` |
| `PermissionService.java` | 权限服务 | `PermissionServiceTest.java` |
| `AuthController.java` | 认证控制器 | `AuthControllerTest.java` |

#### JWT Payload 设计
```json
{
  "sub": "username",
  "userId": 1,
  "roles": ["admin", "user"],
  "posts": ["ceo"],
  "permissions": ["system:user:list", "system:user:add"],
  "iat": 1737014400,
  "exp": 1737016200
}
```

---

### 4.4 阶段 3：核心业务模块

**目标**: 用户/角色/菜单/部门管理 CRUD

#### RESTful API 设计

| 资源 | HTTP 方法 | URL | 说明 | 权限 |
|------|----------|-----|------|------|
| 用户 | GET | /api/system/users | 分页查询 | system:user:list |
| 用户 | GET | /api/system/users/{id} | 详情查询 | system:user:query |
| 用户 | POST | /api/system/users | 新增用户 | system:user:add |
| 用户 | PUT | /api/system/users/{id} | 修改用户 | system:user:edit |
| 用户 | DELETE | /api/system/users/{id} | 删除用户 | system:user:remove |
| 用户 | PUT | /api/system/users/{id}/password | 重置密码 | system:user:resetPwd |
| 角色 | GET | /api/system/roles | 分页查询 | system:role:list |
| 角色 | GET | /api/system/roles/{id} | 详情查询 | system:role:query |
| 角色 | POST | /api/system/roles | 新增角色 | system:role:add |
| 角色 | PUT | /api/system/roles/{id} | 修改角色 | system:role:edit |
| 角色 | DELETE | /api/system/roles/{id} | 删除角色 | system:role:remove |
| 菜单 | GET | /api/system/menus | 树形结构 | system:menu:list |
| 部门 | GET | /api/system/depts | 树形结构 | system:dept:list |

---

### 4.5 阶段 4：系统功能模块

**目标**: 字典/配置/日志/定时任务

#### 日志切面实现

```java
@Aspect
@Component
public class LogAspect {

    // 自动记录所有 Controller 请求
    @Around("execution(* com.xie.glm.admin.controller..*(..))")
    public Object around(ProceedingJoinPoint point) {
        // 记录基础信息：URL、方法名、执行时长、操作人、IP
    }

    // 增强 @Log 注解的方法
    @Around("@annotation(log)")
    public Object aroundWithLog(ProceedingJoinPoint point, Log log) {
        // 额外记录：业务类型、请求参数、响应结果
    }

    // 排除 @NoLog 注解的方法
    @Around("@annotation(com.xie.glm.common.annotation.NoLog)")
    public Object aroundNoLog(ProceedingJoinPoint point) {
        return point.proceed();
    }
}
```

---

### 4.6 阶段 5：高级特性

**目标**: 数据权限/文件上传/导入导出

#### 数据权限设计

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
    String alias() default "";     // 表别名
    String column() = "dept_id";  // 权限字段
}

// 使用示例
@DataScope(alias = "u", column = "dept_id")
public List<SysUser> selectUserList(SysUser user) {
    // AOP 自动在 SQL 中追加数据权限条件
}
```

---

### 4.7 阶段 6：代码生成器

**目标**: Web 界面 + Freemarker 模板

#### API 设计

```
GET    /api/generator/tables           # 获取数据库表列表
GET    /api/generator/tables/{name}    # 获取表详情（列信息）
POST   /api/generator/preview          # 预览生成代码
POST   /api/generator/code/download    # 下载代码（zip）
POST   /api/generator/code/write       # 写入项目目录
```

#### Freemarker 模板变量

```java
// 可在模板中使用的变量
${packageName}      // 包名
${moduleName}        // 模块名
${className}         // 类名
${tableName}         // 表名
${tableComment}      // 表注释
${columns}           // 列信息列表
${author}            // 作者
${datetime}          // 生成时间
```

---

## 5. 验证与测试策略

### 5.1 集成测试为主

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
        "admin, admin123, 200",
        "admin, wrongpass, 401"
    })
    void testLogin(String username, String password, int expectedStatus) throws Exception {
        String loginRequest = """
            {"username":"%s","password":"%s"}
            """.formatted(username, password);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(expectedStatus == 200 ? 0 : 1));
    }
}
```

### 5.2 阶段验证命令

```bash
# 阶段 0 验证
mvn spring-boot:run -pl GLM-admin
curl http://localhost:8080/doc.html

# 阶段 2 验证
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 全量测试
mvn test
```

---

## 6. 风险与缓解措施

| 风险 | 影响 | 缓解措施 |
|------|------|---------|
| JWT 密钥泄露 | 安全漏洞 | 使用环境变量，定期轮换 |
| SQL 注入 | 数据泄露 | MyBatis Plus 预编译，参数化查询 |
| 权限绕过 | 越权访问 | Spring Security + 方法级权限校验 |
| 循环依赖 | 启动失败 | 严格遵循依赖方向，使用构造器注入 |

---

## 7. 新增功能分期规划

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

## 8. 总结

本技术实现方案：

1. **完全符合** `constitution.md` 所有条款要求
2. **严格遵循** TDD 开发流程，测试先行
3. **优先使用** Java 标准库和 Spring Framework 核心组件
4. **采用简洁** 的分层架构，避免过度设计
5. **统一响应** 格式，异常链传递，构造器注入
6. **JWT 双令牌** 无状态认证，声明式权限校验

接下来将按照阶段 0 → 阶段 6 的顺序实施，每个阶段从编写失败的测试开始（Red），然后实现功能使测试通过（Green），最后重构优化（Refactor）。
