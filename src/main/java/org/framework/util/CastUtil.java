package org.framework.util;

/**
 * Created by lizhaoz on 2015/12/16.
 */

public class CastUtil {
    /**
     * 转为 String 型
     */
    public  static  String castString(Object obj){
        return CastUtil.castString(obj,"");
    }
    /**
     * 转为 String 型（提供默认值）
     */
    public static String castString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }
    /**
     * 转为 double 型
     */
    public static double castDouble(Object obj) {
        return CastUtil.castDouble(obj, 0);
    }
    /**
     * 转为 double 型（提供默认值）
     * 先转换为String然后转换为Double
     */
    public static double castDouble(Object obj, double defaultValue) {
        double doubleValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    doubleValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }
    /**
     * 转为 long 型
     */
    public static long castLong(Object obj) {
        return CastUtil.castLong(obj, 0);
    }

    /**
     * 转为 long 型（提供默认值）
     * 先转换为String然后转换为Long
     */
    public static long castLong(Object obj, long defaultValue) {
        long longValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }
    /**
     * 转为 int 型
     */
    public static int castInt(Object obj) {
        return CastUtil.castInt(obj, 0);
    }

    /**
     * 转为 int 型（提供默认值）
     * 先转变为String在转变为int
     */
    public static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }
    /**
     * 转为 boolean 型
     */
    public static boolean castBoolean(Object obj) {
        return CastUtil.castBoolean(obj, false);
    }

    /**
     * 转为 boolean 型（提供默认值）
     * 不需要判断字符串是否为空转换为boolean，如果是转换后为空，会转换为false
     */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if (obj != null) {
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }
}
