package com.xie.glm.common.util;

import lombok.Getter;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 密码校验工具
 *
 * <p>基于 Passay 库实现密码强度校验，提供中文友好的错误提示。
 *
 * <p>密码规则：
 * <ul>
 *   <li>长度：8-32 位</li>
 *   <li>必须包含：大小写字母 + 数字 + 特殊字符</li>
 *   <li>不能包含用户名</li>
 *   <li>不能包含空格</li>
 * </ul>
 *
 * @author xie
 */
@Getter
public class PasswordValidator {

    /**
     * Passay 密码校验器
     */
    private final org.passay.PasswordValidator passayValidator;

    /**
     * 构造方法，初始化校验规则
     */
    public PasswordValidator() {
        List<Rule> rules = new ArrayList<>();

        // 1. 长度规则：8-32 位
        rules.add(new LengthRule(8, 32));

        // 2. 字符规则：至少包含 1 个大写字母
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));

        // 3. 字符规则：至少包含 1 个小写字母
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));

        // 4. 字符规则：至少包含 1 个数字
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));

        // 5. 字符规则：至少包含 1 个特殊字符
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));

        // 6. 不允许包含空格
        rules.add(new WhitespaceRule());

        this.passayValidator = new org.passay.PasswordValidator(rules);
    }

    /**
     * 校验密码
     *
     * @param password 密码
     * @param username 用户名（可为 null，跳过用户名检查）
     * @return 校验结果
     */
    public ValidationResult validate(String password, String username) {
        if (password == null) {
            return ValidationResult.fail(Arrays.asList("密码不能为空"));
        }

        // 检查是否包含用户名
        if (username != null && !username.isEmpty()) {
            if (password.toLowerCase().contains(username.toLowerCase())) {
                return ValidationResult.fail(Arrays.asList("密码不能包含用户名"));
            }
        }

        // 创建密码数据并执行校验
        PasswordData passwordData = new PasswordData(password);
        RuleResult result = passayValidator.validate(passwordData);

        if (result.isValid()) {
            return ValidationResult.success();
        }

        // 转换错误信息为中文
        List<String> errors = new ArrayList<>();
        for (String error : passayValidator.getMessages(result)) {
            errors.add(toChineseMessage(error));
        }

        return ValidationResult.fail(errors);
    }

    /**
     * 将 Passay 错误信息转换为中文
     *
     * @param message Passay 原始错误信息
     * @return 中文错误信息
     */
    private String toChineseMessage(String message) {
        if (message.contains("uppercase")) {
            return "密码必须包含大写字母";
        }
        if (message.contains("lowercase")) {
            return "密码必须包含小写字母";
        }
        if (message.contains("digit")) {
            return "密码必须包含数字";
        }
        if (message.contains("special")) {
            return "密码必须包含特殊字符";
        }
        if (message.contains("whitespace")) {
            return "密码不能包含空格";
        }
        if (message.contains("length")) {
            return "密码长度必须在 8-32 位之间";
        }
        // 默认返回原始消息
        return message;
    }

    /**
     * 校验密码（不检查用户名）
     *
     * @param password 密码
     * @return 校验结果
     */
    public ValidationResult validate(String password) {
        return validate(password, null);
    }
}
