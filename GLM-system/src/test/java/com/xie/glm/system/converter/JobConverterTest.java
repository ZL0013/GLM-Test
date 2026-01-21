package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysJob;
import com.xie.glm.system.dto.JobDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JobConverter 定时任务转换器测试类
 *
 * <p>测试 MapStruct 生成的定时任务对象转换功能
 *
 * @author xie
 */
@DisplayName("JobConverter 定时任务转换器单元测试")
class JobConverterTest {

    private final JobConverter jobConverter = new JobConverterImpl();

    // ==================== Entity → DTO 转换测试 ====================

    @Test
    @DisplayName("Entity → DTO - 验证基本转换")
    void testToDto() {
        // 准备测试数据
        SysJob entity = new SysJob();
        entity.setJobId(1L);
        entity.setJobName("数据清理任务");
        entity.setJobGroup("DEFAULT");
        entity.setInvokeTarget("com.xie.glm.task.DataCleanup.run");
        entity.setInvokeParam("days=30");
        entity.setCronExpression("0 0 2 * * ?");
        entity.setMisfirePolicy("1");
        entity.setConcurrent("0");
        entity.setStatus("0");
        entity.setBeginTime(LocalDateTime.now());
        entity.setNextValidTime(LocalDateTime.now().plusDays(1));
        entity.setRemark("每天凌晨2点执行数据清理");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        // 执行转换
        JobDTO dto = jobConverter.toDto(entity);

        // 验证结果
        assertNotNull(dto, "DTO不应为null");
        assertEquals(entity.getJobId(), dto.getJobId(), "任务ID应匹配");
        assertEquals(entity.getJobName(), dto.getJobName(), "任务名称应匹配");
        assertEquals(entity.getJobGroup(), dto.getJobGroup(), "任务组应匹配");
        assertEquals(entity.getInvokeTarget(), dto.getInvokeTarget(), "调用目标应匹配");
        assertEquals(entity.getInvokeParam(), dto.getInvokeParam(), "调用参数应匹配");
        assertEquals(entity.getCronExpression(), dto.getCronExpression(), "Cron表达式应匹配");
        assertEquals(entity.getMisfirePolicy(), dto.getMisfirePolicy(), "执行策略应匹配");
        assertEquals(entity.getConcurrent(), dto.getConcurrent(), "并发标志应匹配");
        assertEquals(entity.getStatus(), dto.getStatus(), "状态应匹配");
        assertEquals(entity.getBeginTime(), dto.getBeginTime(), "执行开始时间应匹配");
        assertEquals(entity.getNextValidTime(), dto.getNextValidTime(), "下次执行时间应匹配");
        assertEquals(entity.getRemark(), dto.getRemark(), "备注应匹配");
        assertEquals(entity.getCreateTime(), dto.getCreateTime(), "创建时间应匹配");
        assertEquals(entity.getUpdateTime(), dto.getUpdateTime(), "更新时间应匹配");
    }

    @Test
    @DisplayName("Entity → DTO - 验证 null 值处理")
    void testToDtoWithNullValues() {
        SysJob entity = new SysJob();
        // 所有字段保持默认值 null

        JobDTO dto = jobConverter.toDto(entity);

        assertNotNull(dto, "DTO不应为null");
        assertNull(dto.getJobId(), "null字段应保持null");
        assertNull(dto.getJobName(), "null字段应保持null");
        assertNull(dto.getJobGroup(), "null字段应保持null");
    }

    // ==================== DTO → Entity 转换测试 ====================

    @Test
    @DisplayName("DTO → Entity - 验证基本转换")
    void testDtoToEntity() {
        JobDTO dto = new JobDTO();
        dto.setJobId(1L);
        dto.setJobName("数据清理任务");
        dto.setJobGroup("DEFAULT");
        dto.setInvokeTarget("com.xie.glm.task.DataCleanup.run");
        dto.setInvokeParam("days=30");
        dto.setCronExpression("0 0 2 * * ?");
        dto.setMisfirePolicy("1");
        dto.setConcurrent("0");
        dto.setStatus("0");
        dto.setRemark("每天凌晨2点执行数据清理");

        SysJob entity = jobConverter.toEntity(dto);

        assertNotNull(entity, "Entity不应为null");
        assertEquals(dto.getJobId(), entity.getJobId(), "任务ID应匹配");
        assertEquals(dto.getJobName(), entity.getJobName(), "任务名称应匹配");
        assertEquals(dto.getJobGroup(), entity.getJobGroup(), "任务组应匹配");
        assertEquals(dto.getInvokeTarget(), entity.getInvokeTarget(), "调用目标应匹配");
        assertEquals(dto.getInvokeParam(), entity.getInvokeParam(), "调用参数应匹配");
        assertEquals(dto.getCronExpression(), entity.getCronExpression(), "Cron表达式应匹配");
        assertEquals(dto.getMisfirePolicy(), entity.getMisfirePolicy(), "执行策略应匹配");
        assertEquals(dto.getConcurrent(), entity.getConcurrent(), "并发标志应匹配");
        assertEquals(dto.getStatus(), entity.getStatus(), "状态应匹配");
        assertEquals(dto.getRemark(), entity.getRemark(), "备注应匹配");
    }

    // ==================== 列表转换测试 ====================

    @Test
    @DisplayName("Entity List → DTO List - 验证列表转换")
    void testToDtoList() {
        List<SysJob> entities = Arrays.asList(
            createTestJob(1L, "数据清理任务", "DEFAULT"),
            createTestJob(2L, "数据同步任务", "SYSTEM"),
            createTestJob(3L, "报表生成任务", "REPORT")
        );

        List<JobDTO> dtos = jobConverter.toDtoList(entities);

        assertNotNull(dtos, "DTO列表不应为null");
        assertEquals(entities.size(), dtos.size(), "列表大小应匹配");

        for (int i = 0; i < entities.size(); i++) {
            assertEquals(entities.get(i).getJobId(), dtos.get(i).getJobId(),
                "第" + i + "个元素的 jobId 应匹配");
            assertEquals(entities.get(i).getJobName(), dtos.get(i).getJobName(),
                "第" + i + "个元素的 jobName 应匹配");
        }
    }

    @Test
    @DisplayName("Entity List → DTO List - 验证空列表转换")
    void testToDtoListWithEmpty() {
        List<SysJob> entities = List.of();

        List<JobDTO> dtos = jobConverter.toDtoList(entities);

        assertNotNull(dtos, "DTO列表不应为null");
        assertTrue(dtos.isEmpty(), "DTO列表应为空");
    }

    @Test
    @DisplayName("Entity List → DTO List - 验证 null 列表转换")
    void testToDtoListWithNull() {
        List<JobDTO> dtos = jobConverter.toDtoList(null);

        // MapStruct 生成的实现通常返回 null 或空列表
        if (dtos != null) {
            assertTrue(dtos.isEmpty(), "null输入应返回空列表或null");
        }
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 任务列表查询转换")
    void testScenario_JobListQuery() {
        // 模拟数据库查询结果
        List<SysJob> dbResult = Arrays.asList(
            createTestJob(1L, "数据清理任务", "DEFAULT"),
            createTestJob(2L, "数据同步任务", "SYSTEM"),
            createTestJob(3L, "报表生成任务", "REPORT")
        );

        // 转换为 DTO 返回给前端
        List<JobDTO> dtoList = jobConverter.toDtoList(dbResult);

        assertEquals(3, dtoList.size(), "应返回3个任务");

        // 验证第一个任务
        assertEquals(1L, dtoList.get(0).getJobId());
        assertEquals("数据清理任务", dtoList.get(0).getJobName());
        assertEquals("DEFAULT", dtoList.get(0).getJobGroup());
    }

    @Test
    @DisplayName("业务场景 - 创建任务转换")
    void testScenario_CreateJob() {
        JobDTO dto = new JobDTO();
        dto.setJobName("新任务");
        dto.setJobGroup("DEFAULT");
        dto.setInvokeTarget("com.xie.glm.task.NewTask.run");
        dto.setCronExpression("0 0 1 * * ?");
        dto.setStatus("1"); // 暂停

        // 转换为 Entity 准备插入数据库
        SysJob entity = jobConverter.toEntity(dto);

        assertEquals("新任务", entity.getJobName());
        assertEquals("DEFAULT", entity.getJobGroup());
        assertEquals("1", entity.getStatus());
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建测试用的任务实体
     */
    private static SysJob createTestJob(Long jobId, String jobName, String jobGroup) {
        SysJob entity = new SysJob();
        entity.setJobId(jobId);
        entity.setJobName(jobName);
        entity.setJobGroup(jobGroup);
        entity.setInvokeTarget("com.xie.glm.task.TestTask.run");
        entity.setCronExpression("0 0 2 * * ?");
        entity.setMisfirePolicy("1");
        entity.setConcurrent("0");
        entity.setStatus("0");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }
}
