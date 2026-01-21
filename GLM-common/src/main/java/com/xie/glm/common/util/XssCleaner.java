package com.xie.glm.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * XSS 清理工具类
 *
 * <p>用于清理用户输入中的恶意脚本，防止 XSS 攻击。
 * <p>主要清理：
 * <ul>
 *   <li>脚本标签：&lt;script&gt;、&lt;/script&gt;</li>
 *   <li>事件处理器：onclick、onerror、onload 等</li>
 *   <li>JavaScript 协议：javascript:</li>
 *   <li>危险标签：&lt;iframe&gt;、&lt;object&gt;、&lt;embed&gt; 等</li>
 * </ul>
 *
 * @author xie
 */
@Slf4j
public final class XssCleaner {

    private XssCleaner() {
        // 工具类，禁止实例化
    }

    // 脚本标签正则
    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_SELF_CLOSING_PATTERN = Pattern.compile("<script[^>]*/>", Pattern.CASE_INSENSITIVE);

    // 事件处理器正则
    private static final Pattern EVENT_HANDLER_PATTERN = Pattern.compile(
            "\\bon[a-z]+\\s*=\\s*(\"[^\"]*\"|'[^']*'|[^\"'>\\s]+)",
            Pattern.CASE_INSENSITIVE
    );

    // JavaScript 协议正则
    private static final Pattern JS_PROTOCOL_PATTERN = Pattern.compile(
            "javascript:",
            Pattern.CASE_INSENSITIVE
    );

    // 危险标签正则
    private static final Pattern IFRAME_PATTERN = Pattern.compile("<iframe[^>]*>.*?</iframe>", Pattern.CASE_INSENSITIVE);
    private static final Pattern OBJECT_PATTERN = Pattern.compile("<object[^>]*>.*?</object>", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMBED_PATTERN = Pattern.compile("<embed[^>]*>.*?</embed>", Pattern.CASE_INSENSITIVE);

    /**
     * 清理字符串中的 XSS 内容
     *
     * @param input 输入字符串
     * @return 清理后的字符串，如果输入为空则返回原值
     */
    public static String clean(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }

        String cleaned = input;

        // 移除脚本标签
        cleaned = SCRIPT_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = SCRIPT_SELF_CLOSING_PATTERN.matcher(cleaned).replaceAll("");

        // 移除事件处理器
        cleaned = EVENT_HANDLER_PATTERN.matcher(cleaned).replaceAll("");

        // 移除 JavaScript 协议
        cleaned = JS_PROTOCOL_PATTERN.matcher(cleaned).replaceAll("");

        // 移除危险标签
        cleaned = IFRAME_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = OBJECT_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = EMBED_PATTERN.matcher(cleaned).replaceAll("");

        return cleaned;
    }

    /**
     * 清理字符串数组中的 XSS 内容
     *
     * @param inputs 输入字符串数组
     * @return 清理后的字符串数组
     */
    public static String[] clean(String[] inputs) {
        if (inputs == null || inputs.length == 0) {
            return inputs;
        }

        String[] cleaned = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            cleaned[i] = clean(inputs[i]);
        }
        return cleaned;
    }
}
