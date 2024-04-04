package com.liu.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:41
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

//   /**
//    * 根据消息键和参数 获取消息 委托给 MessageSource
//    *
//    * @param code 消息键
//    * @param args 参数
//    * @return 获取国际化翻译值
//    */
//   public static String message(String code, Object... args) {
//       MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
//       return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
//   }

    /**
     * 语言代码的长度 例如 en_US 其中 LANGUAGE_LEN = 2
     */
    private final static int LANGUAGE_LEN = 2;

    /**
     * 进行翻译
     *
     * @param key 通过key ==> user.login.success
     * @return 结果值: 用户登录成功 或者 英文:xxx
     */
    public static String message(String key) {
        return message(key, Locale.getDefault());
    }


    public static String message(String key, String language) {
        if (!StringUtils.isEmpty(language)) {
            String[] arr = language.split("_");
            if (arr.length == LANGUAGE_LEN) {
                return message(key, new Locale(arr[0], arr[1]));
            }
        }
        return message(key, Locale.getDefault());
    }

    public static String message(String key, Object[] params, String language) {
        if (!StringUtils.isEmpty(language)) {
            String[] arr = language.split("_");
            if (arr.length == LANGUAGE_LEN) {
                return message(key, params, new Locale(arr[0], arr[1]));
            }
        }
        return message(key, params, Locale.getDefault());
    }

    private static String message(String key, Locale language) {
        return message(key, new String[0], language);
    }

    private static String message(String key, Object[] params, Locale language) {
        return getInstance().getMessage(key, params, language);
    }

    /**
     * 获取 MessageSource 的单一实例的私有方法
     */
    private static MessageSource getInstance() {
        return Lazy.MESSAGE_SOURCE;
    }


    /**
     * Lazy 类的目的是确保通过延迟初始化获取 MessageSource bean 在需要时获取 MessageSource 的单一实例
     * Lazy 类是静态的，这意味着它与类相关联，而不是与类的实例相关联。
     * 它仅在访问Lazy.MESSAGE_SOURCE时加载到内存中，从而提供延迟初始化
     */
    private static class Lazy {
        private static final MessageSource MESSAGE_SOURCE = SpringUtils.getBean(MessageSource.class);
    }
}
