# GLM-Test 快速开发框架 - 任务列表

> 本任务列表遵循 TDD 原则：**所有实现任务前必须有对应的测试任务**
>
> 任务格式：`[P]` 标记可并行执行的任务

---

## Phase 0: 环境准备

### 0.1 项目配置文件

#### 0.1.1 [P] 创建主配置文件
**文件**: `GLM-admin/src/main/resources/application.yml`
**内容**: Spring Boot 基础配置、数据源、MyBatis Plus、JWT 配置

#### 0.1.2 [P] 创建开发环境配置
**文件**: `GLM-admin/src/main/resources/application-dev.yml`
**内容**: 开发环境特定配置（日志级别、数据库连接）

#### 0.1.3 [P] 创建测试环境配置
**文件**: `GLM-admin/src/main/resources/application-test.yml`
**内容**: 测试环境配置

#### 0.1.4 [P] 创建生产环境配置
**文件**: `GLM-admin/src/main/resources/application-prod.yml`
**内容**: 生产环境配置

#### 0.1.5 [P] 创建 Logback 日志配置
**文件**: `GLM-admin/src/main/resources/logback-spring.xml`
**内容**: 日志级别、文件轮转、敏感信息脱敏

---

### 0.2 主启动类

#### 0.2.1 创建主启动类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/GlmApplication.java`
**内容**: `@SpringBootApplication` 注解，包扫描配置

---

## Phase 1: 公共基础设施

### 1.1 统一响应包装

#### 1.1.1 [P] 创建 Result 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/core/ResultTest.java`
**内容**: `@ParameterizedTest` 测试 success/fail 方法

#### 1.1.2 创建 Result 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/core/Result.java`
**内容**: 统一响应包装类，code/message/data 字段

---

### 1.2 实体基类

#### 1.2.1 [P] 创建 BaseEntity 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/core/BaseEntityTest.java`
**内容**: 实体基类测试

#### 1.2.2 创建 BaseEntity 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/core/BaseEntity.java`
**内容**: id、createTime、updateTime、createdBy、updatedBy 字段

---

### 1.3 分页结果包装

#### 1.3.1 [P] 创建 PageResult 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/core/PageResultTest.java`
**内容**: 分页结果测试

#### 1.3.2 创建 PageResult 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/core/PageResult.java`
**内容**: records、total 字段

---

### 1.4 业务状态码枚举

#### 1.4.1 [P] 创建 BusinessStatus 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/enums/BusinessStatusTest.java`
**内容**: 状态码枚举测试

#### 1.4.2 创建 BusinessStatus 枚举
**文件**: `GLM-common/src/main/java/com/xie/glm/common/enums/BusinessStatus.java`
**内容**: SUCCESS、ERROR、系统模块、用户模块、角色模块、菜单模块、部门模块错误码

#### 1.4.3 [P] 创建 BusinessType 枚举
**文件**: `GLM-common/src/main/java/com/xie/glm/common/enums/BusinessType.java`
**内容**: INSERT、UPDATE、DELETE、EXPORT、OTHER 等

---

### 1.5 业务异常类

#### 1.5.1 [P] 创建 ServiceException 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/exception/ServiceExceptionTest.java`
**内容**: 异常构造和异常链测试

#### 1.5.2 创建 ServiceException 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/exception/ServiceException.java`
**内容**: 继承 RuntimeException，支持异常链

---

### 1.6 安全工具类

#### 1.6.1 [P] 创建 SecurityUtil 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/util/SecurityUtilTest.java`
**内容**: 安全工具类测试

#### 1.6.2 创建 SecurityUtil 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/util/SecurityUtil.java`
**内容**: 获取当前用户、用户ID、权限等工具方法

---

### 1.7 密码校验工具

#### 1.7.1 [P] 创建 PasswordValidator 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/util/PasswordValidatorTest.java`
**内容**: 参数化测试各种密码场景

#### 1.7.2 创建 PasswordValidator 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/util/PasswordValidator.java`
**内容**: 基于 Passay 的密码校验，中文错误提示

---

### 1.8 字符串工具类

#### 1.8.1 [P] 创建 StringUtil 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/util/StringUtilTest.java`
**内容**: 字符串工具测试

#### 1.8.2 创建 StringUtil 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/util/StringUtil.java`
**内容**: 字符串判空、脱敏等工具方法

---

### 1.9 日期工具类

#### 1.9.1 [P] 创建 DateUtil 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/util/DateUtilTest.java`
**内容**: 日期工具测试

#### 1.9.2 创建 DateUtil 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/util/DateUtil.java`
**内容**: 日期格式化、时间戳转换等

---

### 1.10 Excel 工具类

#### 1.10.1 [P] 创建 ExcelUtil 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/util/ExcelUtilTest.java`
**内容**: Excel 导入导出测试

#### 1.10.2 创建 ExcelUtil 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/util/ExcelUtil.java`
**内容**: 基于 FastExcel 的导入导出工具

---

### 1.11 全局异常处理器

#### 1.11.1 [P] 创建 GlobalExceptionHandler 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/web/GlobalExceptionHandlerTest.java`
**内容**: 参数化测试各种异常场景

#### 1.11.2 创建 GlobalExceptionHandler 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/web/GlobalExceptionHandler.java`
**内容**: `@ControllerAdvice` + `@ExceptionHandler` 统一异常处理

---

### 1.12 响应包装处理器

#### 1.12.1 [P] 创建 ResponseAdvice 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/web/ResponseAdviceTest.java**
**内容**: 响应包装测试

#### 1.12.2 创建 ResponseAdvice 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/web/ResponseAdvice.java**
**内容**: `ResponseBodyAdvice` 自动包装返回值

---

### 1.13 MyBatis Plus 配置

#### 1.13.1 [P] 创建 MybatisPlusConfig 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/config/MybatisPlusConfigTest.java`
**内容**: MyBatis Plus 配置测试

#### 1.13.2 创建 MybatisPlusConfig 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/config/MybatisPlusConfig.java`
**内容**: 分页插件、数据权限插件配置


---

### 1.14 Knife4j 配置

#### 1.14.1 [P] 创建 Knife4jConfig 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/config/Knife4jConfigTest.java`
**内容**: Knife4j 配置测试

#### 1.14.2 创建 Knife4jConfig 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/config/Knife4jConfig.java`
**内容**: API 文档配置

---

### 1.15 参数校验配置

#### 1.15.1 [P] 创建 ValidationConfig 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/config/ValidationConfigTest.java`
**内容**: 校验配置测试

#### 1.15.2 创建 ValidationConfig 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/config/ValidationConfig.java`
**内容**: Jakarta Validation 配置

---

### 1.16 自定义校验注解

#### 1.16.1 [P] 创建自定义校验注解测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/validation/CustomValidationTest.java`
**内容**: 自定义校验注解测试

#### 1.16.2 创建 @Phone 注解
**文件**: `GLM-common/src/main/java/com/xie/glm/common/validation/annotation/Phone.java`
**内容**: 手机号校验注解

#### 1.16.3 创建 PhoneValidator 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/validation/validator/PhoneValidator.java`
**内容**: 手机号校验器实现

---

## Phase 2: 认证授权

### 2.1 Spring Security 6 配置

#### 2.1.1 [P] 创建 SecurityConfig 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/config/SecurityConfigTest.java`
**内容**: Security 配置测试

#### 2.1.2 创建 SecurityConfig 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/config/SecurityConfig.java`
**内容**: Spring Security 6 配置、SecurityFilterChain、PasswordEncoder

---

### 2.2 JWT Token 管理器

#### 2.2.1 [P] 创建 JwtTokenManager 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/security/JwtTokenManagerTest.java`
**内容**: JWT 生成、验证、刷新测试

#### 2.2.2 创建 JwtTokenManager 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/JwtTokenManager.java`
**内容**: JWT 双令牌生成、验证、刷新

---

### 2.3 JWT 认证过滤器

#### 2.3.1 [P] 创建 JwtAuthenticationFilter 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/security/JwtAuthenticationFilterTest.java`
**内容**: JWT 过滤器测试

#### 2.3.2 创建 JwtAuthenticationFilter 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/JwtAuthenticationFilter.java`
**内容**: JWT 认证过滤器，继承 OncePerRequestFilter

---

### 2.4 未认证处理器

#### 2.4.1 [P] 创建 JwtAuthenticationEntryPoint 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/security/JwtAuthenticationEntryPointTest.java`
**内容**: 401 响应测试

#### 2.4.2 创建 JwtAuthenticationEntryPoint 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/JwtAuthenticationEntryPoint.java`
**内容**: 实现 AuthenticationEntryPoint，返回 401

---

### 2.5 无权限处理器

#### 2.5.1 [P] 创建 CustomAccessDeniedHandler 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/security/CustomAccessDeniedHandlerTest.java`
**内容**: 403 响应测试

#### 2.5.2 创建 CustomAccessDeniedHandler 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/CustomAccessDeniedHandler.java`
**内容**: 实现 AccessDeniedHandler，返回 403

---

### 2.6 用户详情服务

#### 2.6.1 [P] 创建 UserDetailsServiceImpl 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/security/UserDetailsServiceImplTest.java`
**内容**: 用户详情加载测试

#### 2.6.2 创建 UserDetailsServiceImpl 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/UserDetailsServiceImpl.java`
**内容**: 实现 UserDetailsService，加载用户权限

---

### 2.7 自定义用户详情

#### 2.7.1 创建 CustomUserDetails 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/security/CustomUserDetailsTest.java`
**内容**: 自定义用户详情测试

#### 2.7.2 创建 CustomUserDetails 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/CustomUserDetails.java`
**内容**: 自定义 UserDetails，包含 userId、roles、permissions

---

### 2.8 用户实体

#### 2.8.1 [P] 创建 SysUser 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysUserTest.java`
**内容**: 用户实体测试

#### 2.8.2 创建 SysUser 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysUser.java`
**内容**: 用户实体，用户名、密码、邮箱、部门ID、状态等字段

---

### 2.9 角色实体

#### 2.9.1 [P] 创建 SysRole 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysRoleTest.java`
**内容**: 角色实体测试

#### 2.9.2 创建 SysRole 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysRole.java`
**内容**: 角色实体，角色名、权限标识、状态等字段

---

### 2.10 菜单实体

#### 2.10.1 [P] 创建 SysMenu 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysMenuTest.java`
**内容**: 菜单实体测试

#### 2.10.2 创建 SysMenu 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysMenu.java`
**内容**: 菜单实体，菜单名、父菜单ID、路由、权限标识等字段

---

### 2.11 认证控制器

#### 2.11.1 [P] 创建 AuthController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/AuthControllerTest.java`
**内容**: 登录、刷新令牌、登出测试

#### 2.11.2 创建 AuthController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/AuthController.java`
**内容**: 登录接口、刷新令牌接口、登出接口

---

## Phase 3: 核心业务模块

### 3.1 用户管理

#### 3.1.1 [P] 创建 UserQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/UserQueryDTOTest.java`
**内容**: 查询条件 DTO 测试

#### 3.1.2 创建 UserQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/UserQueryDTO.java`
**内容**: 用户查询条件 DTO

---

#### 3.1.3 [P] 创建 UserCreateDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/UserCreateDTOTest.java`
**内容**: 创建用户 DTO 测试

#### 3.1.4 创建 UserCreateDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/UserCreateDTO.java`
**内容**: 创建用户 DTO，包含用户基本信息、角色ID列表、岗位ID列表

---

#### 3.1.5 [P] 创建 UserUpdateDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/UserUpdateDTOTest.java`
**内容**: 更新用户 DTO 测试

#### 3.1.6 创建 UserUpdateDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/UserUpdateDTO.java`
**内容**: 更新用户 DTO

---

#### 3.1.7 [P] 创建 UserDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/UserDTOTest.java`
**内容**: 用户 DTO 测试

#### 3.1.8 创建 UserDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/UserDTO.java`
**内容**: 用户 DTO，返回给前端的数据

---

#### 3.1.9 [P] 创建 UserMapper 接口测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/mapper/SysUserMapperTest.java`
**内容**: Mapper 接口测试

#### 3.1.10 创建 SysUserMapper 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/mapper/SysUserMapper.java`
**内容**: 用户 Mapper 接口

---

#### 3.1.11 [P] 创建 UserService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/UserServiceTest.java`
**内容**: 用户服务测试

#### 3.1.12 创建 IUserService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IUserService.java`
**内容**: 用户服务接口

#### 3.1.13 创建 UserServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/UserServiceImpl.java`
**内容**: 用户服务实现，包含创建、更新、查询、删除、重置密码

---

#### 3.1.14 [P] 创建 UserConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/UserConverterTest.java`
**内容**: MapStruct 转换器测试

#### 3.1.15 创建 UserConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/UserConverter.java`
**内容**: Entity ↔ DTO 转换器

---

#### 3.1.16 [P] 创建 UserCreateBO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/bo/UserCreateBOTest.java`
**内容**: BO 测试

#### 3.1.17 创建 UserCreateBO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/bo/UserCreateBO.java`
**内容**: 创建用户 BO，组合用户基本信息与角色、岗位关联数据

---

### 3.2 用户 VO 和转换器

#### 3.2.1 [P] 创建 UserVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/UserVOTest.java`
**内容**: 用户 VO 测试

#### 3.2.2 创建 UserVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/UserVO.java`
**内容**: 用户 VO，前端展示数据

---

#### 3.2.3 [P] 创建 UserVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/UserVoConverterTest.java`
**内容**: VO 转换器测试

#### 3.2.4 创建 UserVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/UserVoConverter.java`
**内容**: DTO → VO 转换器

---

### 3.3 用户 Facade

#### 3.3.1 [P] 创建 UserFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/UserFacadeTest.java`
**内容**: Facade 测试

#### 3.3.2 创建 UserFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/UserFacade.java`
**content**: 用户门面，封装 Service 调用 + DTO → VO 转换

---

### 3.4 用户控制器

#### 3.4.1 [P] 创建 UserController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/UserControllerTest.java`
**内容**: 控制器测试

#### 3.4.2 创建 UserController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/UserController.java`
**内容**: 用户 CRUD + 重置密码控制器

---

### 3.5 角色管理

#### 3.5.1 [P] 创建 RoleQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/RoleQueryDTOTest.java`
**内容**: 角色查询 DTO 测试

#### 3.5.2 创建 RoleQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/RoleQueryDTO.java`
**内容**: 角色查询条件 DTO

---

#### 3.5.3 [P] 创建 RoleCreateDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/RoleCreateDTOTest.java`
**内容**: 创建角色 DTO 测试

#### 3.5.4 创建 RoleCreateDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/RoleCreateDTO.java`
**内容**: 创建角色 DTO

---

#### 3.5.5 [P] 创建 RoleUpdateDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/RoleUpdateDTOTest.java`
**内容**: 更新角色 DTO 测试

#### 3.5.6 创建 RoleUpdateDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/RoleUpdateDTO.java`
**内容**: 更新角色 DTO

---

#### 3.5.7 [P] 创建 RoleDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/RoleDTOTest.java`
**内容**: 角色 DTO 测试

#### 3.5.8 创建 RoleDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/RoleDTO.java`
**内容**: 角色 DTO

---

#### 3.5.9 [P] 创建 SysRoleMapper 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/mapper/SysRoleMapperTest.java`
**内容**: Mapper 接口测试

#### 3.5.10 创建 SysRoleMapper 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/mapper/SysRoleMapper.java`
**内容**: 角色 Mapper 接口

---

#### 3.5.11 [P] 创建 IRoleService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/RoleServiceTest.java`
**内容**: 角色服务测试

#### 3.5.12 创建 IRoleService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IRoleService.java`
**内容**: 角色服务接口

#### 3.5.13 创建 RoleServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/RoleServiceImpl.java`
**内容**: 角色服务实现

---

#### 3.5.14 [P] 创建 RoleConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/RoleConverterTest.java`
**内容**: 角色转换器测试

#### 3.5.15 创建 RoleConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/RoleConverter.java`
**内容**: 角色 Entity ↔ DTO 转换器

---

#### 3.5.16 [P] 创建 RoleMenuAssignBO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/bo/RoleMenuAssignBOTest.java`
**内容**: BO 测试

#### 3.5.17 创建 RoleMenuAssignBO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/bo/RoleMenuAssignBO.java`
**内容**: 角色菜单权限分配 BO

---

### 3.6 角色 VO 和转换器

#### 3.6.1 [P] 创建 RoleVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/RoleVOTest.java`
**内容**: 角色 VO 测试

#### 3.6.2 创建 RoleVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/RoleVO.java`
**内容**: 角色 VO

---

#### 3.6.3 [P] 创建 RoleVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/RoleVoConverterTest.java`
**内容**: 角色转换器测试

#### 3.6.4 创建 RoleVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/RoleVoConverter.java`
**内容**: 角色 DTO → VO 转换器

---

### 3.7 角色 Facade

#### 3.7.1 [P] 创建 RoleFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/RoleFacadeTest.java`
**内容**: 角色门面测试

#### 3.7.2 创建 RoleFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/RoleFacade.java`
**content**: 角色门面

---

### 3.8 角色控制器

#### 3.8.1 [P] 创建 RoleController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/RoleControllerTest.java`
**内容**: 角色控制器测试

#### 3.8.2 创建 RoleController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/RoleController.java`
**内容**: 角色控制器

---

### 3.9 菜单管理

#### 3.9.1 [P] 创建 MenuQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/MenuQueryDTOTest.java`
**内容**: 菜单查询 DTO 测试

#### 3.9.2 创建 MenuQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/MenuQueryDTO.java`
**内容**: 菜单查询条件 DTO

---

#### 3.9.3 [P] 创建 MenuDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/MenuDTOTest.java`
**内容**: 菜单 DTO 测试

#### 3.9.4 创建 MenuDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/MenuDTO.java`
**内容**: 菜单 DTO

---

#### 3.9.5 [P] 创建 SysMenuMapper 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/mapper/SysMenuMapperTest.java`
**内容**: 菜单 Mapper 测试

#### 3.9.6 创建 SysMenuMapper 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/mapper/SysMenuMapper.java`
**内容**: 菜单 Mapper 接口

---

#### 3.9.7 [P] 创建 IMenuService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/MenuServiceTest.java`
**内容**: 菜单服务测试

#### 3.9.8 创建 IMenuService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IMenuService.java`
**内容**: 菜单服务接口

#### 3.9.9 创建 MenuServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/MenuServiceImpl.java`
**内容**: 菜单服务实现，树形结构处理

---

#### 3.9.10 [P] 创建 MenuConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/MenuConverterTest.java`
**内容**: 菜单转换器测试

#### 3.9.11 创建 MenuConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/MenuConverter.java`
**内容**: 菜单 Entity ↔ DTO 转换器

---

### 3.10 菜单 VO 和转换器

#### 3.10.1 [P] 创建 MenuVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/MenuVOTest.java`
**内容**: 菜单 VO 测试

#### 3.10.2 创建 MenuVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/MenuVO.java`
**内容**: 菜单 VO，包含子菜单列表

---

#### 3.10.3 [P] 创建 MenuVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/MenuVoConverterTest.java`
**内容**: 菜单转换器测试

#### 3.10.4 创建 MenuVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/MenuVoConverter.java`
**内容**: 菜单 DTO → VO 转换器

---

### 3.11 菜单 Facade

#### 3.11.1 [P] 创建 MenuFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/MenuFacadeTest.java`
**内容**: 菜单门面测试

#### 3.11.2 创建 MenuFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/MenuFacade.java`
**content**: 菜单门面

---

### 3.12 菜单控制器

#### 3.12.1 [P] 创建 MenuController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/MenuControllerTest.java`
**内容**: 菜单控制器测试

#### 3.12.2 创建 MenuController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/MenuController.java`
**content**: 菜单控制器

---

### 3.13 部门管理

#### 3.13.1 [P] 创建 DeptQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/DeptQueryDTOTest.java`
**内容**: 部门查询 DTO 测试

#### 3.13.2 创建 DeptQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/DeptQueryDTO.java`
**内容**: 部门查询条件 DTO

---

#### 3.13.3 [P] 创建 DeptDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/DeptDTOTest.java`
**内容**: 部门 DTO 测试

#### 3.13.4 创建 DeptDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/DeptDTO.java`
**内容**: 部门 DTO

---

#### 3.13.5 [P] 创建 SysDeptMapper 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/mapper/SysDeptMapperTest.java`
**内容**: 部门 Mapper 测试

#### 3.13.6 创建 SysDeptMapper 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/mapper/SysDeptMapper.java`
**内容**: 部门 Mapper 接口

---

#### 3.13.7 [P] 创建 IDeptService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/DeptServiceTest.java`
**内容**: 部门服务测试

#### 3.13.8 创建 IDeptService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IDeptService.java`
**内容**: 部门服务接口

#### 3.13.9 创建 DeptServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/DeptServiceImpl.java`
**content**: 部门服务实现，树形结构处理

---

#### 3.13.10 [P] 创建 DeptConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/DeptConverterTest.java`
**内容**: 部门转换器测试

#### 3.13.11 创建 DeptConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/DeptConverter.java`
**内容**: 部门 Entity ↔ DTO 转换器

---

### 3.14 部门 VO 和转换器

#### 3.14.1 [P] 创建 DeptVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/DeptVOTest.java`
**内容**: 部门 VO 测试

#### 3.14.2 创建 DeptVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/DeptVO.java`
**content**: 部门 VO，包含子部门列表

---

#### 3.14.3 [P] 创建 DeptVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/DeptVoConverterTest.java`
**内容**: 部门转换器测试

#### 3.14.4 创建 DeptVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/DeptVoConverter.java`
**content**: 部门 DTO → VO 转换器

---

### 3.15 部门 Facade

#### 3.15.1 [P] 创建 DeptFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/DeptFacadeTest.java`
**内容**: 部门门面测试

#### 3.15.2 创建 DeptFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/DeptFacade.java`
**content**: 部门门面

---

### 3.16 部门控制器

#### 3.16.1 [P] 创建 DeptController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/DeptControllerTest.java`
**内容**: 部门控制器测试

#### 3.16.2 创建 DeptController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/DeptController.java`
**content**: 部门控制器

---

## Phase 4: 系统功能模块

### 4.1 字典管理

#### 4.1.1 [P] 创建 SysDictType 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysDictTypeTest.java`
**内容**: 字典类型实体测试

#### 4.1.2 创建 SysDictType 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysDictType.java`
**content**: 字典类型实体

---

#### 4.1.3 [P] 创建 SysDictData 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysDictDataTest.java`
**内容**: 字典数据实体测试

#### 4.1.4 创建 SysDictData 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysDictData.java`
**content**: 字典数据实体

---

#### 4.1.5 [P] 创建 DictQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/DictQueryDTOTest.java`
**内容**: 字典查询 DTO 测试

#### 4.1.6 创建 DictQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/DictQueryDTO.java`
**content**: 字典查询条件 DTO

---

#### 4.1.7 [P] 创建 DictDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/DictDTOTest.java`
**内容**: 字典 DTO 测试

#### 4.1.8 创建 DictDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/DictDTO.java`
**content**: 字典 DTO

---

#### 4.1.9 [P] 创建 SysDictTypeMapper 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/mapper/SysDictTypeMapperTest.java`
**内容**: 字典类型 Mapper 测试

#### 4.1.10 创建 SysDictTypeMapper 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/mapper/SysDictTypeMapper.java`
**content**: 字典类型 Mapper 接口

---

#### 4.1.11 [P] 创建 IDictService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/DictServiceTest.java`
**内容**: 字典服务测试

#### 4.1.12 创建 IDictService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IDictService.java`
**content**: 字典服务接口

#### 4.1.13 创建 DictServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/DictServiceImpl.java`
**content**: 字典服务实现

---

#### 4.1.14 [P] 创建 DictConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/DictConverterTest.java`
**内容**: 字典转换器测试

#### 4.1.15 创建 DictConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/DictConverter.java`
**content**: 字典 Entity ↔ DTO 转换器

---

#### 4.1.16 [P] 创建 DictVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/DictVOTest.java`
**内容**: 字典 VO 测试

#### 4.1.17 创建 DictVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/DictVO.java`
**content**: 字典 VO

---

#### 4.1.18 [P] 创建 DictVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/DictVoConverterTest.java`
**内容**: 字典转换器测试

#### 4.1.19 创建 DictVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/DictVoConverter.java`
**内容**: 字典 DTO → VO 转换器

---

#### 4.1.20 [P] 创建 DictFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/DictFacadeTest.java`
**内容**: 字典门面测试

#### 4.1.21 创建 DictFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/DictFacade.java`
**content**: 字典门面

---

#### 4.1.22 [P] 创建 DictController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/DictControllerTest.java`
**内容**: 字典控制器测试

#### 4.1.23 创建 DictController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/DictController.java`
**content**: 字典控制器

---

### 4.2 参数配置管理

#### 4.2.1 [P] 创建 SysConfig 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysConfigTest.java`
**内容**: 配置实体测试

#### 4.2.2 创建 SysConfig 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysConfig.java`
**content**: 参数配置实体

---

#### 4.2.3 [P] 创建 ConfigQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/ConfigQueryDTOTest.java`
**内容**: 配置查询 DTO 测试

#### 4.2.4 创建 ConfigQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/ConfigQueryDTO.java`
**内容**: 配置查询条件 DTO

---

#### 4.2.5 [P] 创建 ConfigDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/ConfigDTOTest.java`
**内容**: 配置 DTO 测试

#### 4.2.6 创建 ConfigDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/ConfigDTO.java`
**内容**: 配置 DTO

---

#### 4.2.7 [P] 创建 IConfigService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/IConfigServiceTest.java`
**内容**: 配置服务测试

#### 4.2.8 创建 IConfigService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IConfigService.java`
**内容**: 配置服务接口

#### 4.2.9 创建 ConfigServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/ConfigServiceImpl.java`
**content**: 配置服务实现

---

#### 4.2.10 [P] 创建 ConfigConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/ConfigConverterTest.java`
**内容**: 配置转换器测试

#### 4.2.11 创建 ConfigConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/ConfigConverter.java`
**内容**: 配置 Entity ↔ DTO 转换器

---

#### 4.2.12 [P] 创建 ConfigVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/ConfigVOTest.java`
**内容**: 配置 VO 测试

#### 4.2.13 创建 ConfigVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/ConfigVO.java`
**content**: 配置 VO

---

#### 4.2.14 [P] 创建 ConfigVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/ConfigVoConverterTest.java`
**内容**: 配置转换器测试

#### 4.2.15 创建 ConfigVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/ConfigVoConverter.java`
**content**: 配置 DTO → VO 转换器

---

#### 4.2.16 [P] 创建 ConfigFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/ConfigFacadeTest.java`
**内容**: 配置门面测试

#### 4.2.17 创建 ConfigFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/ConfigFacade.java`
**content**: 配置门面

---

#### 4.2.18 [P] 创建 ConfigController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/ConfigControllerTest.java`
**内容**: 配置控制器测试

#### 4.2.19 创建 ConfigController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/ConfigController.java`
**content**: 配置控制器

---

### 4.3 日志管理

#### 4.3.1 [P] 创建 SysOperLog 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysOperLogTest.java`
**内容**: 操作日志实体测试

#### 4.3.2 创建 SysOperLog 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysOperLog.java`
**内容**: 操作日志实体

---

#### 4.3.3 [P] 创建 SysLoginInfo 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysLoginInfoTest.java`
**内容**: 登录日志实体测试

#### 4.3.4 创建 SysLoginInfo 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysLoginInfo.java`
**内容**: 登录日志实体

---

#### 4.3.5 [P] 创建 LogQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/LogQueryDTOTest.java`
**内容**: 日志查询 DTO 测试

#### 4.3.6 创建 LogQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/LogQueryDTO.java`
**内容**: 日志查询条件 DTO

---

#### 4.3.7 [P] 创建 OperLogDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/OperLogDTOTest.java`
**内容**: 操作日志 DTO 测试

#### 4.3.8 创建 OperLogDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/OperLogDTO.java`
**content**: 操作日志 DTO

---

#### 4.3.9 [P] 创建 IOperLogService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/IOperLogServiceTest.java`
**内容**: 操作日志服务测试

#### 4.3.10 创建 IOperLogService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IOperLogService.java`
**content**: 操作日志服务接口

#### 4.3.11 创建 OperLogServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/OperLogServiceImpl.java`
**content**: 操作日志服务实现

---

#### 4.3.12 [P] 创建 OperLogConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/OperLogConverterTest.java`
**内容**: 操作日志转换器测试

#### 4.3.13 创建 OperLogConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/OperLogConverter.java`
**content**: 操作日志 Entity ↔ DTO 转换器

---

#### 4.3.14 [P] 创建 OperLogVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/OperLogVOTest.java`
**内容**: 操作日志 VO 测试

#### 4.3.15 创建 OperLogVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/OperLogVO.java`
**content**: 操作日志 VO

---

#### 4.3.16 [P] 创建 OperLogVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/OperLogVoConverterTest.java`
**内容**: 操作日志转换器测试

#### 4.3.17 创建 OperLogVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/OperLogVoConverter.java`
**content**: 操作日志 DTO → VO 转换器

---

#### 4.3.18 [P] 创建 OperLogFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/OperLogFacadeTest.java`
**内容**: 操作日志门面测试

#### 4.3.19 创建 OperLogFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/OperLogFacade.java`
**content**: 操作日志门面

---

#### 4.3.20 [P] 创建 LogController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/LogControllerTest.java`
**内容**: 日志控制器测试

#### 4.3.21 创建 LogController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/LogController.java`
**content**: 日志控制器

---

### 4.4 日志切面

#### 4.4.1 [P] 创建 LogAspect 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/aspect/LogAspectTest.java`
**内容**: 日志切面测试

#### 4.4.2 创建 LogAnnotation 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/annotation/LogAnnotationTest.java`
**内容**: @Log 注解测试

#### 4.4.3 创建 @Log 注解
**文件**: `GLM-common/src/main/java/com/xie/glm/common/annotation/Log.java`
**内容**: 日志注解，包含 title、businessType、保存参数配置

#### 4.4.4 创建 @NoLog 注解
**文件**: `GLM-common/src/main/java/com/xie/glm/common/annotation/NoLog.java`
**内容**: 排除日志注解

#### 4.4.5 创建 LogAspect 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/aspect/LogAspect.java`
**内容**: 日志切面，自动记录 + @Log 增强

---

### 4.5 定时任务管理

#### 4.5.1 [P] 创建 SysJob 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysJobTest.java`
**内容**: 定时任务实体测试

#### 4.5.2 创建 SysJob 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysJob.java`
**content**: 定时任务实体

---

#### 4.5.3 [P] 创建 SysJobLog 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysJobLogTest.java`
**内容**: 任务日志实体测试

#### 4.5.4 创建 SysJobLog 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysJobLog.java`
**content**: 任务日志实体

---

#### 4.5.5 [P] 创建 JobQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/JobQueryDTOTest.java`
**内容**: 任务查询 DTO 测试

#### 4.5.6 创建 JobQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/JobQueryDTO.java`
**内容**: 任务查询条件 DTO

---

#### 4.5.7 [P] 创建 JobDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/JobDTOTest.java`
**内容**: 任务 DTO 测试

#### 4.5.8 创建 JobDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/JobDTO.java`
**内容**: 任务 DTO

---

#### 4.5.9 [P] 创建 IJobService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/IJobServiceTest.java`
**内容**: 任务服务测试

#### 4.5.10 创建 IJobService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IJobService.java`
**内容**: 任务服务接口

#### 4.5.11 创建 JobServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/JobServiceImpl.java`
**content**: 任务服务实现，动态任务管理

---

#### 4.5.12 [P] 创建 JobConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/JobConverterTest.java`
**内容**: 任务转换器测试

#### 4.5.13 创建 JobConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/JobConverter.java`
**content**: 任务 Entity ↔ DTO 转换器

---

#### 4.5.14 [P] 创建 JobVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/JobVOTest.java`
**内容**: 任务 VO 测试

#### 4.5.15 创建 JobVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/JobVO.java`
**content**: 任务 VO

---

#### 4.5.16 [P] 创建 JobVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/JobVoConverterTest.java`
**内容**: 任务转换器测试

#### 4.5.17 创建 JobVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/JobVoConverter.java`
**content**: 任务 DTO → VO 转换器

---

#### 4.5.18 [P] 创建 JobFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/JobFacadeTest.java`
**内容**: 任务门面测试

#### 4.5.19 创建 JobFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/JobFacade.java`
**content**: 任务门面

---

#### 4.5.20 [P] 创建 JobController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/JobControllerTest.java`
**内容**: 任务控制器测试

#### 4.5.21 创建 JobController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/JobController.java`
**content**: 任务控制器

---

### 4.6 通知公告管理

#### 4.6.1 [P] 创建 SysNotice 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/SysNoticeTest.java`
**内容**: 通知实体测试

#### 4.6.2 创建 SysNotice 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/SysNotice.java`
**content**: 通知公告实体

---

#### 4.6.3 [P] 创建 NoticeQueryDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/query/NoticeQueryDTOTest.java`
**内容**: 通知查询 DTO 测试

#### 4.6.4 创建 NoticeQueryDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/query/NoticeQueryDTO.java`
**内容**: 通知查询条件 DTO

---

#### 4.6.5 [P] 创建 NoticeDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/NoticeDTOTest.java`
**内容**: 通知 DTO 测试

#### 4.6.6 创建 NoticeDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/NoticeDTO.java`
**内容**: 通知 DTO

---

#### 4.6.7 [P] 创建 INoticeService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/INoticeServiceTest.java`
**内容**: 通知服务测试

#### 4.6.8 创建 INoticeService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/INoticeService.java`
**content**: 通知服务接口

#### 4.6.9 创建 NoticeServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/NoticeServiceImpl.java`
**content**: 通知服务实现

---

#### 4.6.10 [P] 创建 NoticeConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/NoticeConverterTest.java`
**内容**: 通知转换器测试

#### 4.6.11 创建 NoticeConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/NoticeConverter.java`
**内容**: 通知 Entity ↔ DTO 转换器

---

#### 4.6.12 [P] 创建 NoticeVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/NoticeVOTest.java`
**内容**: 通知 VO 测试

#### 4.6.13 创建 NoticeVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/NoticeVO.java`
**content**: 通知 VO

---

#### 4.6.14 [P] 创建 NoticeVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/NoticeVoConverterTest.java`
**内容**: 通知转换器测试

#### 4.6.15 创建 NoticeVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/NoticeVoConverter.java`
**content**: 通知 DTO → VO 转换器

---

#### 4.6.16 [P] 创建 NoticeFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/NoticeFacadeTest.java`
**内容**: 通知门面测试

#### 4.6.17 创建 NoticeFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/NoticeFacade.java`
**content**: 通知门面

---

#### 4.6.18 [P] 创建 NoticeController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/NoticeControllerTest.java`
**内容**: 通知控制器测试

#### 4.6.19 创建 NoticeController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/NoticeController.java`
**content**: 通知控制器

---

## Phase 5: 高级特性

### 5.1 数据权限（MyBatis Plus 插件）

#### 5.1.1 [P] 创建 DataPermissionHandler 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/security/DataPermissionHandlerTest.java`
**内容**: 数据权限处理器测试

#### 5.1.2 创建 DataPermissionHandler 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/DataPermissionHandler.java`
**内容**: 实现 IDataPermissionHandler，根据用户数据权限构造 SQL 条件

---

#### 5.1.3 [P] 创建 @DataScope 注解测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/annotation/DataScopeTest.java`
**内容**: 数据权限注解测试

#### 5.1.4 创建 @DataScope 注解
**文件**: `GLM-common/src/main/java/com/xie/glm/common/annotation/DataScope.java`
**内容**: 数据权限注解，配合 MP 插件使用

---

### 5.2 文件上传下载

#### 5.2.1 [P] 创建 FileUploadDTO 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/dto/FileUploadDTOTest.java`
**内容**: 文件上传 DTO 测试

#### 5.2.2 创建 FileUploadDTO 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/dto/FileUploadDTO.java`
**内容**: 文件上传 DTO

---

#### 5.2.3 [P] 创建 FileQueryDTO 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/dto/FileQueryDTOTest.java**
**内容**: 文件查询 DTO 测试

#### 5.2.4 创建 FileQueryDTO 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/dto/FileQueryDTO.java`
**内容**: 文件查询条件 DTO

---

#### 5.2.5 [P] 创建 FileDTO 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/dto/FileDTOTest.java`
**内容**: 文件 DTO 测试

#### 5.2.6 创建 FileDTO 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/dto/FileDTO.java`
**content**: 文件 DTO

---

#### 5.2.7 [P] 创建 IFileService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/IFileServiceTest.java`
**内容**: 文件服务测试

#### 5.2.8 创建 IFileService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IFileService.java`
**content**: 文件服务接口

#### 5.2.9 创建 FileServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/FileServiceImpl.java`
**content**: 文件服务实现

---

#### 5.2.10 [P] 创建 FileConverter 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/converter/FileConverterTest.java`
**内容**: 文件转换器测试

#### 5.2.11 创建 FileConverter 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/converter/FileConverter.java`
**内容**: 文件 Entity ↔ DTO 转换器

---

#### 5.2.12 [P] 创建 FileVO 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/vo/FileVOTest.java`
**内容**: 文件 VO 测试

#### 5.2.13 创建 FileVO 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/vo/FileVO.java`
**content**: 文件 VO

---

#### 5.2.14 [P] 创建 FileVoConverter 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/converter/FileVoConverterTest.java**
**内容**: 文件转换器测试

#### 5.2.15 创建 FileVoConverter 接口
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/converter/FileVoConverter.java`
**content**: 文件 DTO → VO 转换器

---

#### 5.2.16 [P] 创建 FileFacade 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/facade/FileFacadeTest.java`
**内容**: 文件门面测试

#### 5.2.17 创建 FileFacade 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/facade/FileFacade.java`
**content**: 文件门面

---

#### 5.2.18 [P] 创建 FileController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/FileControllerTest.java`
**内容**: 文件控制器测试

#### 5.2.19 创建 FileController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/FileController.java`
**content**: 文件控制器

---

### 5.3 Excel 导入导出

#### 5.3.1 [P] 创建 @Excel 注解测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/annotation/ExcelTest.java`
**内容**: Excel 注解测试

#### 5.3.2 创建 @Excel 注解
**文件**: `GLM-common/src/main/java/com/xie/glm/common/annotation/Excel.java`
**内容**: Excel 导入导出注解

---

#### 5.3.3 [P] 创建 ExcelUtil 测试
**文件**: `GLM-common/src/test/java/com/xie/glm/common/util/ExcelUtilTest.java`
**内容**: Excel 工具测试

#### 5.3.4 创建 ExcelUtil 类
**文件**: `GLM-common/src/main/java/com/xie/glm/common/util/ExcelUtil.java`
**内容**: 基于 FastExcel 的导入导出工具

---

## Phase 6: 代码生成器

### 6.1 代码生成服务

#### 6.1.1 [P] 创建 GeneratorConfig 测试
**文件**: `GLM-generator/src/test/java/com/xie/glm/core/model/GeneratorConfigTest.java`
**content**: 生成配置测试

#### 6.1.2 创建 GeneratorConfig 类
**文件**: `GLM-generator/src/main/java/com/xie/glm/core/model/GeneratorConfig.java`
**content**: 生成配置模型

---

#### 6.1.3 [P] 创建 TableInfo 测试
**文件**: `GLM-generator/src/test/java/com/xie/glm/core/model/TableInfoTest.java`
**content**: 表信息测试

#### 6.1.4 创建 TableInfo 类
**文件**: `GLM-generator/src/main/java/com/xie/glm/core/model/TableInfo.java`
**content**: 表信息模型

---

#### 6.1.5 [P] 创建 ColumnInfo 测试
**文件**: `GLM-generator/src/test/java/com/xie/glm/core/model/ColumnInfoTest.java`
**content**: 列信息测试

#### 6.1.6 创建 ColumnInfo 类
**文件**: `GLM-generator/src/main/java/com/xie/glm/core/model/ColumnInfo.java`
**content**: 列信息模型

---

#### 6.1.7 [P] 创建 DatabaseMetaData 测试
**文件**: `GLM-generator/src/test/java/com/xie/glm/core/DatabaseMetaDataTest.java`
**内容**: 数据库元数据测试

#### 6.1.8 创建 DatabaseMetaData 类
**文件**: `GLM-generator/src/main/java/com/xie/glm/core/DatabaseMetaData.java`
**content**: 数据库元数据读取

---

#### 6.1.9 [P] 创建 CodeGenerator 测试
**文件**: `GLM-generator/src/test/java/com/xie/glm/core/CodeGeneratorTest.java`
**内容**: 代码生成引擎测试

#### 6.1.10 创建 CodeGenerator 类
**文件**: `GLM-generator/src/main/java/com/xie/glm/core/CodeGenerator.java`
**内容**: 代码生成引擎

---

#### 6.1.11 [P] 创建 GeneratorService 测试
**文件**: `GLM-generator/src/test/java/com/xie/glm/service/GeneratorServiceTest.java`
**内容**: 生成服务测试

#### 6.1.12 创建 GeneratorService 类
**文件**: `GLM-generator/src/main/java/com/xie/glm/service/GeneratorService.java`
**content**: 代码生成服务

---

#### 6.1.13 [P] 创建 GeneratorController 测试
**文件**: `GLM-generator/src/test/java/com/xie/glm/controller/GeneratorControllerTest.java`
**内容**: 生成控制器测试

#### 6.1.14 创建 GeneratorController 类
**文件**: `GLM-generator/src/main/java/com/xie/glm/controller/GeneratorController.java`
**content**: 生成控制器

---

### 6.2 Freemarker 模板

#### 6.2.1 [P] 创建 entity.ftl 模板
**文件**: `GLM-generator/src/main/resources/templates/entity.ftl`
**内容**: Entity 类模板

---

#### 6.2.2 [P] 创建 mapper.ftl 模板
**文件**: `GLM-generator/src/main/resources/templates/mapper.ftl`
**内容**: Mapper 接口模板

---

#### 6.2.3 [P] 创建 service.ftl 模板
**文件**: `GLM-generator/src/main/resources/templates/service.ftl`
**内容**: Service 接口模板

---

#### 6.2.4 [P] 创建 serviceImpl.ftl 模板
**文件**: `GLM-generator/src/main/resources/templates/serviceImpl.ftl`
**content**: Service 实现类模板

---

#### 6.2.5 [P] 创建 controller.ftl 模板
**文件**: `GLM-generator/src/main/resources/templates/controller.ftl`
**内容**: Controller 模板

---

#### 6.2.6 [P] 创建 dto.ftl 模板
**文件**: `GLM-generator/src/main/resources/templates/dto.ftl`
**内容**: DTO 模板

---

#### 6.2.7 [P] 创建 vo.ftl 模板
**文件**: `GLM-generator/src/main/resources/templates/vo.ftl`
**content**: VO 模板

---

#### 6.2.8 [P] 创建 converter.ftl 模板
**文件**: `GLM-generator/src/main/resources/templates/converter.ftl`
**内容**: Converter 模板

---

### 6.3 API 设计

#### 6.3.1 [P] 创建 代码生成器 API 文档测试
**文件**: `GLM-generator/src/test/java/com/xie/glm/controller/GeneratorControllerTest.java`
**内容**: 代码生成器 API 测试

#### 6.3.2 创建 API 文档
**文件**: `GLM-generator/docs/api.md`
**内容**: 代码生成器 API 文档

---

## Phase 7: 运维优化（可选）

### 7.1 在线用户管理

#### 7.1.1 [P] 创建 OnlineUser 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/OnlineUserTest.java`
**内容**: 在线用户实体测试

#### 7.1.2 创建 OnlineUser 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/OnlineUser.java`
**内容**: 在线用户实体

---

#### 7.1.3 [P] 创建 TokenStore 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/security/TokenStoreTest.java`
**内容**: Token 存储测试

#### 7.1.4 创建 TokenStore 接口
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/TokenStore.java`
**content**: Token 存储接口

#### 7.1.5 创建 InMemoryTokenStore 实现类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/security/InMemoryTokenStore.java`
**内容**: 内存 Token 存储

---

### 7.2 登录限流

#### 7.2.1 [P] 创建 LoginAttempt 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/domain/LoginAttemptTest.java`
**内容**: 登录尝试实体测试

#### 7.2.2 创建 LoginAttempt 实体类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/domain/LoginAttempt.java`
**内容**: 登录尝试记录实体

---

#### 7.2.3 [P] 创建 RateLimitAspect 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/aspect/RateLimitAspectTest.java`
**内容**: 限流切面测试

#### 7.2.4 创建 @RateLimit 注解
**文件**: `GLM-common/src/main/java/com/xie/glm/common/annotation/RateLimit.java`
**内容**: 限流注解

---

#### 7.2.5 创建 RateLimitAspect 类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/aspect/RateLimitAspect.java`
**content**: 限流切面实现

---

### 7.3 密码重置

#### 7.3.1 [P] 创建 PasswordResetDTO 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/dto/PasswordResetDTOTest.java`
**内容**: 密码重置 DTO 测试

#### 7.3.2 创建 PasswordResetDTO 类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/dto/PasswordResetDTO.java`
**内容**: 密码重置 DTO

---

#### 7.3.3 [P] 创建 IPasswordResetService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/IPasswordResetServiceTest.java`
**内容**: 密码重置服务测试

#### 7.3.4 创建 IPasswordResetService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IPasswordResetService.java`
**内容**: 密码重置服务接口

#### 7.3.5 创建 PasswordResetServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/PasswordResetServiceImpl.java`
**内容**: 密码重置服务实现

---

#### 7.3.6 [P] 创建 PasswordResetController 测试
**文件**: `GLM-admin/src/test/java/com/xie/glm/admin/controller/PasswordResetControllerTest.java`
**内容**: 密码重置控制器测试

#### 7.3.7 创建 PasswordResetController 类
**文件**: `GLM-admin/src/main/java/com/xie/glm/admin/controller/PasswordResetController.java`
**content**: 密码重置控制器

---

### 7.4 动态定时任务管理

#### 7.4.1 [P] 创建 DynamicJobService 测试
**文件**: `GLM-system/src/test/java/com/xie/glm/system/service/DynamicJobServiceTest.java`
**内容**: 动态任务服务测试

#### 7.4.2 创建 IDynamicJobService 接口
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/IDynamicJobService.java`
**内容**: 动态任务服务接口

#### 7.4.3 创建 DynamicJobServiceImpl 实现类
**文件**: `GLM-system/src/main/java/com/xie/glm/system/service/impl/DynamicJobServiceImpl.java`
**内容**: 动态任务服务实现

---

### 7.5 文件存储策略

#### 7.5.1 [P] 创建 FileStorageStrategy 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/storage/FileStorageStrategyTest.java`
**内容**: 文件存储策略测试

#### 7.5.2 创建 IFileStorageStrategy 接口
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/storage/IFileStorageStrategy.java`
**内容**: 文件存储策略接口

#### 7.5.3 创建 LocalFileStorage 实现类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/storage/impl/LocalFileStorage.java`
**content**: 本地文件存储实现

---

#### 7.5.4 [P] 创建 OssFileStorage 实现类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/storage/impl/OssFileStorage.java`
**content**: OSS 文件存储实现

---

#### 7.5.5 [P] 创建 MinioFileStorage 实现类
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/storage/impl/MinioFileStorage.java`
**content**: MinIO 文件存储实现

---

### 7.6 敏感配置加密

#### 7.6.1 [P] 创建 JasyptConfig 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/config/JasyptConfigTest.java`
**内容**: 配置加密测试

#### 7.6.2 创建 JasyptConfig 配置
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/config/JasyptConfig.java`
**内容**: Jasypt 配置，加密数据库密码等敏感信息

---

### 7.7 异步任务

#### 7.7.1 [P] 创建 AsyncConfig 测试
**文件**: `GLM-framework/src/test/java/com/xie/glm/framework/config/AsyncConfigTest.java`
**内容**: 异步配置测试

#### 7.7.2 创建 AsyncConfig 配置
**文件**: `GLM-framework/src/main/java/com/xie/glm/framework/config/AsyncConfig.java`
**内容**: @EnableAsync 配置，线程池配置

---

### 7.8 故障排查指南

#### 7.8.1 创建 troubleshooting.md
**文件**: `docs/troubleshooting.md`
**内容**:
- 启动失败排查
- 数据库连接问题
- JWT 认证问题
- 缓存问题
- 性能问题排查
- 常见错误码对照表

---

## 任务统计

| 阶段 | 任务数量 | 说明 |
|------|---------|------|
| Phase 0: 环境准备 | 7 | 配置文件 + 启动类 |
| Phase 1: 公共基础设施 | 47 | 核心、工具类、配置、异常处理 |
| Phase 2: 认证授权 | 24 | Security 6、JWT、用户实体、认证控制器 |
| Phase 3: 核心业务模块 | 140+ | 用户、角色、菜单、部门的 CRUD + Facade/VO/Converter |
| Phase 4: 系统功能模块 | 80+ | 字典、配置、日志、任务、通知管理 |
| Phase 5: 高级特性 | 40+ | 数据权限、文件、Excel |
| Phase 6: 代码生成器 | 25+ | 代码生成器 |
| Phase 7: 运维优化 | 40+ | 在线用户、限流、密码重置、动态任务、文件存储、配置加密、异步、故障排查 |
| **总计** | **400+ 任务** | |

---

## 任务命名规范

- 测试文件：`{ClassName}Test.java`
- 实体/DTO/VO：`{ClassName}.java`
- Mapper：`{Entity}Mapper.java`
- Service 接口：`I{Entity}Service.java`
- Service 实现：`{Entity}ServiceImpl.java`
- Controller：`{Entity}Controller.java`
- Facade：`{Entity}Facade.java`
- Converter：`{Entity}Converter.java`

---

## TDD 强制示例

### 错误示例（禁止）
```
❌ 创建 UserServiceImpl（缺少测试）
✅ 先创建 UserServiceTest（测试驱动）
```

### 正确示例（推荐）
```
✅ 1. UserServiceTest.java（测试用例，预期失败）
✅ 2. UserServiceImpl.java（实现代码，使测试通过）
✅ 3. UserServiceImpl.java（重构优化）
```

---

## 任务依赖关系示例

```
任务依赖链：
SysUserTest → SysUser.java → SysUserMapper.java → IUserService.java → UserServiceImpl.java
                                         ↓
                                    UserDTO.java → UserFacade.java → UserController.java
```

可并行任务：
SysUserTest [P]
SysRoleTest [P]
SysMenuTest [P]
```