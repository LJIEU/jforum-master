package com.liu.core.utils;

import java.util.UUID;

/**
 * Description: UUID处理
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 22:24
 */
public class UUIDUtils {

    /**
     * 获取去除 - 的UUID
     */
    public static String noLinesFormat() {
        String uuid = UUID.randomUUID().toString();
        // 2cde71b3-e272-4dd1-9999-881a1f198b00
        String[] split = uuid.split("-");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(s);
        }
        return sb.toString();
    }
}
