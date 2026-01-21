package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志数据传输对象
 *
 * <p>用于服务层返回给 Facade 层的操作日志数据，包含操作日志的所有字段信息。
 *
 * <p>字段说明：
 * <ul>
 *   <li>operId：日志主键</li>
 *   <li>title：模块标题</li>
 *   <li>businessType：业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）</li>
 *   <li>businessTypeName：业务类型名称</li>
 *   <li>method：方法名称</li>
 *   <li>requestMethod：请求方式</li>
 *   <li>operatorType：操作类别（0其它 1后台用户 2手机端用户）</li>
 *   <li>operatorTypeName：操作类别名称</li>
 *   <li>operName：操作人员</li>
 *   <li>deptName：部门名称</li>
 *   <li>operUrl：请求URL</li>
 *   <li>operIp：主机地址</li>
 *   <li>operLocation：操作地点</li>
 *   <li>operParam：请求参数</li>
 *   <li>jsonResult：返回参数</li>
 *   <li>status：操作状态（0正常 1异常）</li>
 *   <li>statusName：操作状态名称</li>
 *   <li>errorMsg：错误消息</li>
 *   <li>operTime：操作时间</li>
 *   <li>costTime：消耗时间(毫秒)</li>
 * </ul>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OperLogDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    private Long operId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）
     */
    private Integer businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private Integer operatorType;

    /**
     * 操作类别名称
     */
    private String operatorTypeName;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 操作状态名称
     */
    private String statusName;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private LocalDateTime operTime;

    /**
     * 消耗时间(毫秒)
     */
    private Long costTime;
}
