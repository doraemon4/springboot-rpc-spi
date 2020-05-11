package com.stephen.learning.blade.core.sender;

import com.stephen.learning.blade.core.RemoteInfo;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 14:54
 * @Version: 1.0
 */
public interface Sender {
    /**
     * 发送请求
     * @param msg
     * @param remoteInfo
     * @return
     */
    String send(Object msg, RemoteInfo remoteInfo);
}
