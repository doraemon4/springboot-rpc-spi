package com.stephen.learning.blade.core.encoder;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 14:42
 * @Version: 1.0
 */
public interface Encoder {
    /**
     * 实体编码
     * @param arg
     * @return
     */
    Object encode(Object arg);

}