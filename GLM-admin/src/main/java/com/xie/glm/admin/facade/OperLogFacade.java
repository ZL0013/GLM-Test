package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.OperLogVoConverter;
import com.xie.glm.admin.vo.OperLogVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.OperLogDTO;
import com.xie.glm.system.dto.query.LogQueryDTO;
import com.xie.glm.system.service.IOperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志门面类
 *
 * <p>封装操作日志管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 OperLogVoConverter 将 DTO 转换为 VO</li>
 *   <li>设置展示用的文本字段（如 statusText、businessTypeText）</li>
 *   <li>简化 Controller 的逻辑</li>
 * </ul>
 *
 * <p>数据流向：
 * <pre>
 * Controller → Facade → Service → Mapper
 *     ↓         ↓         ↓
 *   VO  ←  VO  ←  DTO
 * </pre>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class OperLogFacade {

    private final IOperLogService operLogService;
    private final OperLogVoConverter voConverter;

    /**
     * 分页查询操作日志列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<OperLogVO> listOperLogs(LogQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<OperLogDTO> dtoPage = operLogService.listOperLogs(query);

        // 转换 DTO 为 VO
        List<OperLogVO> voList = voConverter.toVoList(dtoPage.getRecords());

        // 设置展示用文本字段
        voList.forEach(vo -> this.setDisplayText(vo));

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 根据 ID 查询操作日志详情
     *
     * @param operId 操作日志 ID
     * @return 操作日志 VO
     */
    public OperLogVO getOperLogById(Long operId) {
        // 调用 Service 获取 DTO
        OperLogDTO dto = operLogService.getOperLogById(operId);
        if (dto == null) {
            return null;
        }

        // 转换 DTO 为 VO
        OperLogVO vo = voConverter.toVo(dto);

        // 设置展示用文本字段
        setDisplayText(vo);

        return vo;
    }

    /**
     * 删除操作日志
     *
     * @param operId 操作日志 ID
     */
    public void deleteOperLog(Long operId) {
        operLogService.deleteOperLog(operId);
    }

    /**
     * 批量删除操作日志
     *
     * @param operIds 操作日志 ID 数组
     */
    public void deleteOperLogs(Long[] operIds) {
        operLogService.deleteOperLogs(operIds);
    }

    /**
     * 清空所有操作日志
     */
    public void cleanOperLogs() {
        operLogService.cleanOperLogs();
    }

    /**
     * 设置展示用文本字段
     *
     * @param vo 操作日志 VO
     */
    private void setDisplayText(OperLogVO vo) {
        // 设置状态文本
        if (vo.getStatus() != null) {
            vo.setStatusText(getStatusText(vo.getStatus()));
        }

        // 设置业务类型文本
        if (vo.getBusinessType() != null) {
            vo.setBusinessTypeText(getBusinessTypeText(vo.getBusinessType()));
        }
    }

    /**
     * 获取状态文本
     *
     * @param status 状态码
     * @return 状态文本
     */
    private String getStatusText(Integer status) {
        if (status == 0) {
            return "成功";
        } else if (status == 1) {
            return "失败";
        }
        return "未知";
    }

    /**
     * 获取业务类型文本
     *
     * @param businessType 业务类型码
     * @return 业务类型文本
     */
    private String getBusinessTypeText(Integer businessType) {
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
}
