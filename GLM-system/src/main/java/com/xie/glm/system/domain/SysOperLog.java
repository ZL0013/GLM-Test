package com.xie.glm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.glm.common.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 操作日志实体
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("xie_tm.sys_oper_log")
public class SysOperLog extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId(value = "oper_id", type = IdType.AUTO)
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

    /**
     * 业务类型枚举
     */
    public enum BusinessType {
        /**
         * 其它
         */
        OTHER(0, "其它"),
        /**
         * 新增
         */
        INSERT(1, "新增"),
        /**
         * 修改
         */
        UPDATE(2, "修改"),
        /**
         * 删除
         */
        DELETE(3, "删除"),
        /**
         * 授权
         */
        GRANT(4, "授权"),
        /**
         * 导出
         */
        EXPORT(5, "导出"),
        /**
         * 导入
         */
        IMPORT(6, "导入"),
        /**
         * 强退
         */
        FORCE(7, "强退"),
        /**
         * 生成代码
         */
        GENCODE(8, "生成代码"),
        /**
         * 清空数据
         */
        CLEAN(9, "清空数据");

        private final Integer code;
        private final String name;

        BusinessType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 操作人类别枚举
     */
    public enum OperatorType {
        /**
         * 其它
         */
        OTHER(0, "其它"),
        /**
         * 后台用户
         */
        BACKEND(1, "后台用户"),
        /**
         * 手机端用户
         */
        MOBILE(2, "手机端用户");

        private final Integer code;
        private final String name;

        OperatorType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 操作状态枚举
     */
    public enum Status {
        /**
         * 成功
         */
        SUCCESS(0, "成功"),
        /**
         * 失败
         */
        FAIL(1, "失败");

        private final Integer code;
        private final String name;

        Status(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
