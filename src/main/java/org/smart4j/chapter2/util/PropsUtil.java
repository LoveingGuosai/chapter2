package org.smart4j.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件工具类
 */
public final class PropsUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载文件属性
     */
    public static Properties loadProps(String fileName){
        Properties properties = null;
        InputStream is=null;
        try {
            is=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(is==null){
                throw new FileNotFoundException(fileName+" file is not found");
            }
            properties=new Properties();
            properties.load(is);
        }catch (IOException e){
            logger.error("load properties file failure",e);
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("close input stream failure",e);
                }
            }
        }
        return properties;
    }

    /**
     * 获取字符型属性(默认值是空白字符串)
     */
    public static String getString(Properties properties,String key){
        return getString(properties,key,"");
    }
    /**
     * 获取字符型属性，可指定默认值
     */
    public static  String getString(Properties properties,String key,String defaultValue){
        String value=defaultValue;
        if (properties.containsKey(key)) {
            value=properties.getProperty(key);
        }
        return value;
    }
    /**
     * 获取数值型属性（默认为0）
     */
    public  static int getInt(Properties properties,String key){
        return getInt(properties,key,0);
    }
    /**
     * 获取数值型属性可以设置默认值
     */

    private static int getInt(Properties properties, String key, int defaultVaule) {
        int value=defaultVaule;
        if(properties.containsKey(key)){
            value=CastUtil.castInt(properties.getProperty(key));
        }
        return value;
    }
    /**
     * 获取布尔类型属性 默认值false
     */
    public static boolean getBoolean(Properties properties,String key){
        return getBoolean(properties,key,false);
    }
    /**
     * 获取布尔类型属性 可以设置默认值
     */
    private static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value=defaultValue;
        if(properties.containsKey(key)){
            value=CastUtil.castBoolean(properties.getProperty(key));
        }
        return value;
    }


}
