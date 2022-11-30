package com.peckot.app.AuroraBot;

import java.util.Arrays;

public final class Utils {

    /**
     * @param value 预替换的字符串
     * @param num   明文显示位数
     * @return java.lang.String
     * @author FeianLing
     * @date 2019/10/23
     * @desc 对敏感信息进行加密，对于value字符串num位后面的值全部使用*号代替
     */
    public static String encryptAfterThree(String value, int num) {
        if (null == value || value.length() <= num) return value;
        char[] arr = value.toCharArray();
        Arrays.fill(arr, num, arr.length, '*');
        return new String(arr);
    }

}
