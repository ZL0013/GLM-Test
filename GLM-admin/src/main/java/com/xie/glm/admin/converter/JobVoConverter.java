package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.JobVO;
import com.xie.glm.system.dto.JobDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 定时任务 VO 转换器
 *
 * <p>负责将 JobDTO 转换为 JobVO（前端展示）
 *
 * <p>转换说明：
 * <ul>
 *   <li>statusText、misfirePolicyText、concurrentText 需要在 Facade 层根据对应码手动设置</li>
 *   <li>其他字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(componentModel = "spring")
public interface JobVoConverter {

    /**
     * DTO 转 VO
     *
     * <p>将 JobDTO 转换为 JobVO，用于前端展示
     *
     * <p>注意：statusText、misfirePolicyText、concurrentText 需要在 Facade 层手动设置
     *
     * @param dto 任务 DTO
     * @return 任务 VO
     */
    JobVO toVo(JobDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * <p>将 JobDTO 列表转换为 JobVO 列表
     *
     * @param dtos 任务 DTO 列表
     * @return 任务 VO 列表
     */
    List<JobVO> toVoList(List<JobDTO> dtos);
}
