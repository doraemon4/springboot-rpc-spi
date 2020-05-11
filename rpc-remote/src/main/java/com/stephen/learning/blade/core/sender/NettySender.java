package com.stephen.learning.blade.core.sender;

import com.stephen.learning.blade.core.RemoteInfo;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 15:15
 * @Version: 1.0
 */
public class NettySender implements Sender{
    @Override
    public String send(Object msg, RemoteInfo remoteInfo) {
        System.out.println("使用Netty发送：" + msg);
        String[] host_port = remoteInfo.getHost().split(":");
        NettyClient client = new NettyClient(host_port[0], Integer.valueOf(host_port[1]));
        try {
            return client.send(msg.toString());
        } catch (Exception e) {
            throw new RuntimeException("rpc调用异常", e);
        }
    }
}
