package com.stephen.learning.blade.util;

import com.stephen.learning.blade.annotation.Addition;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 16:01
 * @Version: 1.0
 */
public final class AdditionPropParser {
    public static final ConcurrentHashMap<String, Properties> cachedPropMap = new ConcurrentHashMap<>();

    private AdditionPropParser() {
    }

    public static String getValue(String source, String key){
        return getValue(source, key, null);
    }

    public static String getValue(String source, String key, String defaultValue){
        Object value = parse(source).get(key);
        if(value == null || StringUtils.isEmpty(source)){
            return defaultValue;
        }

        return (String) value;
    }

    public static Properties parse(Addition addition){
        if(addition == null){
            return null;
        }
        return parse(addition.value());
    }

    public static Properties parse(String source) {
        if(StringUtils.isEmpty(source)){
            throw new IllegalStateException("source串不能为空");
        }

        if(cachedPropMap.get(source) == null){
            synchronized(source){
                if(cachedPropMap.get(source) == null){
                    Properties props = new Properties();
                    String[] kvs = source.split("&");
                    for(String kv : kvs){
                        String[] arr = kv.split("=");
                        if(arr.length != 2){
                            throw new IllegalStateException("配置错误：" + kv);
                        }
                        props.setProperty(arr[0], arr[1]);
                    }
                    cachedPropMap.put(source, props);
                }
            }
        }
        return cachedPropMap.get(source);
    }
}