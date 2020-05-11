package com.stephen.learning.blade.core.sender;

import com.stephen.learning.blade.core.RemoteInfo;

import java.io.*;
import java.net.Socket;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 15:17
 * @Version: 1.0
 */
public class SocketSender implements Sender{
    @Override
    public String send(Object msg, RemoteInfo remoteInfo) {
        System.out.println("使用Socket发送：" + msg);
        String[] host_port = remoteInfo.getHost().split(":");
        try (Socket socket = new Socket(host_port[0], Integer.valueOf(host_port[1])); OutputStream os = socket.getOutputStream(); InputStream is = socket.getInputStream();) {
            // 发送数据包
            PrintWriter pw = new PrintWriter(os);
            pw.write(msg.toString());
            pw.flush();
            socket.shutdownOutput();

            // 接收返回
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer info = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("我是客户端，服务器说：" + info);
                info.append(line);
            }
            return info.toString();
        } catch (Exception e) {
            throw new RuntimeException("Socket发送消息异常！", e);
        }

    }
}
