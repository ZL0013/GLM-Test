package com.xie.glm.common.validation;

import com.xie.glm.common.validation.annotation.Phone;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * PhoneValidator 测试类
 *
 * <p>测试手机号校验注解的各种场景
 *
 * @author xie
 */
@DisplayName("手机号校验器测试")
class PhoneValidatorTest {

    private Validator validator;

    /**
     * 测试用的内部类，用于测试 @Phone 注解
     */
    static class TestDto {
        @Phone(message = "手机号格式不正确")
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "13800138000",    // 中国移动
            "13912345678",    // 中国移动
            "15012345678",    // 中国联通
            "18612345678",    // 中国联通
            "18812345678",    // 中国移动
            "17712345678",    // 中国电信
            "19912345678"     // 中国电信
    })
    @DisplayName("验证合法的手机号 - 应该通过校验")
    void testValidPhoneNumbers(String phone) {
        TestDto dto = new TestDto();
        dto.setPhone(phone);

        Set<ConstraintViolation<TestDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size(), () ->
                String.format("手机号 %s 应该是合法的，但校验失败: %s", phone, violations));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "   ",             // 只有空格
            "12345",           // 太短
            "123456789012",    // 太长
            "abcdefghijk",     // 非数字
            "1380013800a",     // 包含字母
            "138-0013-8000",   // 包含特殊字符
            "+8613800138000",  // 带国际区号（当前不支持）
            "10800138000"      // 1开头但第二位是0
    })
    @DisplayName("验证非法的手机号 - 应该校验失败")
    void testInvalidPhoneNumbers(String phone) {
        TestDto dto = new TestDto();
        dto.setPhone(phone);

        Set<ConstraintViolation<TestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), () ->
                String.format("手机号 %s 应该是非法的，但校验通过", phone));

        assertEquals("手机号格式不正确", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("验证空值 - 默认应该通过校验（非必填）")
    void testNullAndEmptyValues(String phone) {
        TestDto dto = new TestDto();
        dto.setPhone(phone);

        Set<ConstraintViolation<TestDto>> violations = validator.validate(dto);
        // 空值默认应该通过校验（非必填）
        assertEquals(0, violations.size(), () ->
                String.format("空值应该通过校验（非必填），但校验失败: %s", violations));
    }
}
