package com.peckot.app.AuroraBot.utils;

import java.util.Arrays;

public final class Strs {

    /**
     * 对敏感信息字符串进行加密.
     * @param value         预替换的字符串
     * @param num           明文显示位数
     * @param replacement   隐藏替换字符
     * @return {@link String} 处理后的字符串
     * @author FeianLing
     */
    public static String encryptAfterThree(String value, int num, char replacement) {
        if (null == value || value.length() <= num) return value;
        char[] arr = value.toCharArray();
        Arrays.fill(arr, num, arr.length, replacement);
        return new String(arr);
    }

}
