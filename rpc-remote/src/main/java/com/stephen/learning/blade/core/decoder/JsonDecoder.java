package com.stephen.learning.blade.core.decoder;

import com.alibaba.fastjson.JSON;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 14:05
 * @Version: 1.0
 */
public class JsonDecoder implements Decoder {
    @Override
    public <T> T decode(String arg, Class<T> returnClass) {
        return JSON.parseObject(arg, returnClass);
    }
}
