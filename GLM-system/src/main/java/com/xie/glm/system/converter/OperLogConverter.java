package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysOperLog;
import com.xie.glm.system.dto.OperLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 操作日志对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 Entity 和 DTO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toDto：Entity → DTO（用于查询结果返回）</li>
 *   <li>toEntity：DTO → Entity（用于保存操作日志）</li>
 *   <li>toDtoList：Entity List → DTO List（用于列表查询）</li>
 * </ul>
 *
 * <p>注意事项：
 * <ul>
 *   <li>业务类型名称和状态名称通过 @Mapping 方法自动转换</li>
 *   <li>所有字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OperLogConverter {

    /**
     * Entity → DTO 转换
     *
     * <p>用于将数据库查询结果转换为返回给 Facade 层的 DTO
     *
     * @param entity 操作日志实体
     * @return 操作日志 DTO
     */
    @Mapping(target = "businessTypeName", source = "businessType", qualifiedByName = "businessTypeToName")
    @Mapping(target = "operatorTypeName", source = "operatorType", qualifiedByName = "operatorTypeToName")
    @Mapping(target = "statusName", source = "status", qualifiedByName = "statusToName")
    OperLogDTO toDto(SysOperLog entity);

    /**
     * Entity List → DTO List 转换
     *
     * <p>用于将操作日志列表转换为 DTO 列表
     *
     * @param entities 操作日志实体列表
     * @return 操作日志 DTO 列表
     */
    @Mapping(target = "businessTypeName", source = "businessType", qualifiedByName = "businessTypeToName")
    @Mapping(target = "operatorTypeName", source = "operatorType", qualifiedByName = "operatorTypeToName")
    @Mapping(target = "statusName", source = "status", qualifiedByName = "statusToName")
    List<OperLogDTO> toDtoList(List<SysOperLog> entities);

    /**
     * DTO → Entity 转换
     *
     * <p>用于保存操作日志时将 DTO 转换为 Entity
     *
     * @param dto 操作日志 DTO
     * @return 操作日志实体
     */
    SysOperLog toEntity(OperLogDTO dto);

    /**
     * 业务类型代码转名称
     *
     * @param businessType 业务类型代码
     * @return 业务类型名称
     */
    @Named("businessTypeToName")
    default String businessTypeToName(Integer businessType) {
        if (businessType == null) {
            return null;
        }
        return switch (businessType) {
            case 0 -> "其它";
            case 1 -> "新增";
            case 2 -> "修改";
            case 3 -> "删除";
            case 4 -> "授权";
            case 5 -> "导出";
            case 6 -> "导入";
            case 7 -> "强退";
            case 8 -> "生成代码";
            case 9 -> "清空数据";
            default -> "未知";
        };
    }

    /**
     * 操作人类别代码转名称
     *
     * @param operatorType 操作人类别代码
     * @return 操作人类别名称
     */
    @Named("operatorTypeToName")
    default String operatorTypeToName(Integer operatorType) {
        if (operatorType == null) {
            return null;
        }
        return switch (operatorType) {
            case 0 -> "其它";
            case 1 -> "后台用户";
            case 2 -> "手机端用户";
            default -> "未知";
        };
    }

    /**
     * 状态代码转名称
     *
     * @param status 状态代码
     * @return 状态名称
     */
    @Named("statusToName")
    default String statusToName(Integer status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case 0 -> "成功";
            case 1 -> "失败";
            default -> "未知";
        };
    }
}
