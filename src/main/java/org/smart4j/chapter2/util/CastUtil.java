package org.smart4j.chapter2.util;

/**
 * 转型操作工具类
 */
public final class CastUtil {
    /**
     * 转为String类型
     */
    public  static String castString(Object object){
        return castString(object,"");
    }
    /**
     * 转为String 可以设置默认值
     */
    public static String castString(Object object,String defaultValue){
        return object!=null?String.valueOf(object):defaultValue;
    }

    /**
     * 转为double型
     */
    public static double castDouble(Object object){
        return castDouble(object, 0);
    }

    /**
     * 转为double类型 可以设置默认值
     */
    private static double castDouble(Object object, double defaultValue) {
        double value=defaultValue;
        if(object!=null){
            String strValue=castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    value=Double.parseDouble(strValue);
                }catch (NumberFormatException e){
                    value=defaultValue;
                }
            }
        }
        return value;
    }
    /**
     *转为long类型
     */
    public static long castLong(Object object){
        return castLong(object, 0);
    }

    /**
     * 转为double类型 可以设置默认值
     */
    private static long castLong(Object object, long defaultValue) {
        long value=defaultValue;
        if(object!=null){
            String strValue=castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    value=Long.parseLong(strValue);
                }catch (NumberFormatException e){
                    value=defaultValue;
                }
            }
        }
        return value;
    }
    /**
     *转为int类型
     */
    public static int castInt(Object object){
        return castInt(object, 0);
    }

    /**
     * 转为int类型 可以设置默认值
     */
    private static int castInt(Object object, int defaultValue) {
        int value=defaultValue;
        if(object!=null){
            String strValue=castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    value=Integer.parseInt(strValue);
                }catch (NumberFormatException e){
                    value=defaultValue;
                }
            }
        }
        return value;
    }
    /**
     *转为boolean类型
     */
    public static boolean castBoolean(Object object){
        return castBoolean(object, false);
    }

    /**
     * 转为boolean类型 可以设置默认值
     */
    private static boolean castBoolean(Object object, boolean defaultValue) {
        boolean value=defaultValue;
        if(object!=null){
            value=Boolean.parseBoolean(castString(object));
        }
        return value;
    }


}
