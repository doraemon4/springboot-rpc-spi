package com.stephen.learning.blade.core.encoder;

import com.alibaba.fastjson.JSON;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 14:43
 * @Version: 1.0
 */
public class JsonEncoder implements Encoder {
    @Override
    public Object encode(Object arg) {
        return JSON.toJSONString(arg);
    }
}
