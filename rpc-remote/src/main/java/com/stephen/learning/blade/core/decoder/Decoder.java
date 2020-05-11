package com.stephen.learning.blade.core.decoder;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 14:04
 * @Version: 1.0
 */
public interface Decoder {
    /**
     * 解码参数
     * @param arg
     * @param returnClass
     * @param <T>
     * @return
     */
    <T> T decode(String arg, Class<T> returnClass);
}
