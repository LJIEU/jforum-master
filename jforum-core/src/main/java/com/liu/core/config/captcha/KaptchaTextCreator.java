package com.liu.core.config.captcha;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * Description: 验证码文字创建
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 18:48
 */
public class KaptchaTextCreator extends DefaultTextCreator {
    private static final String[] NUMBERS = "0,1,2,3,4,5,6,7,8,9,10".split(",");

    @Override
    public String getText() {
        Integer result;
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        StringBuilder compute = new StringBuilder();
        int sign = random.nextInt(3);
        switch (sign) {
            case 0 -> {
                result = x + y;
                compute.append(NUMBERS[x]);
                compute.append("+");
                compute.append(NUMBERS[y]);
            }
            case 1 -> {
                result = x - y;
                compute.append(NUMBERS[x]);
                compute.append("-");
                compute.append(NUMBERS[y]);
            }
            case 2 -> {
                result = x * y;
                compute.append(NUMBERS[x]);
                compute.append("*");
                compute.append(NUMBERS[y]);
            }
            default -> {
                // 避免 除0异常
                while (y == 0 && x == 0) {
                    x = random.nextInt(10);
                    y = random.nextInt(10);
                }
                // 交换元素
                if (y == 0) {
                    x = x + y;
                    y = x - y;
                    x = x - y;
                }
                result = x / y;
                compute.append(NUMBERS[x]);
                compute.append("/");
                compute.append(NUMBERS[y]);
            }
        }
        compute.append("=?@").append(result);
        return compute.toString();
    }
}

