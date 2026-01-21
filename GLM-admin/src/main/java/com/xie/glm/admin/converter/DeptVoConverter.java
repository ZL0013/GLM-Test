package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.DeptVO;
import com.xie.glm.system.dto.DeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 部门视图对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 DTO 和 VO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toVo：DTO → VO（用于返回给前端展示）</li>
 *   <li>toVoList：DTO List → VO List（用于列表查询）</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DeptVoConverter {

    /**
     * DTO → VO 转换
     *
     * <p>用于将服务层返回的 DTO 转换为前端展示的 VO
     *
     * @param dto 部门 DTO
     * @return 部门 VO
     */
    DeptVO toVo(DeptDTO dto);

    /**
     * DTO List → VO List 转换
     *
     * <p>用于将部门 DTO 列表转换为 VO 列表
     *
     * @param dtoList 部门 DTO 列表
     * @return 部门 VO 列表
     */
    List<DeptVO> toVoList(List<DeptDTO> dtoList);
}
