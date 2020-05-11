package com.stephen.learning.blade.core.sender;

import com.alibaba.fastjson.JSON;
import com.stephen.learning.blade.core.RemoteInfo;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author: jack
 * @Description: HTTP请求
 * @Date: 2020/5/11 14:58
 * @Version: 1.0
 */
public class HttpSender implements Sender{
    private static final Logger logger = LoggerFactory.getLogger(HttpSender.class);
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    @Override
    public String send(Object msg, RemoteInfo remoteInfo) {
        String type = remoteInfo.getAdditionProps().getProperty("type");
        String url = getUrl(remoteInfo);

        logger.info("post http request, url:{}, msg:{}", url, JSON.toJSONString(msg));
        if(type == null || "GET".equals(type.toUpperCase())){
            return doGet(url, msg);
        } else {
            return doPost(url, msg);
        }
    }

    private String doPost(String url, Object msg) {
        String jsonMsg = JSON.toJSONString(msg);
        RequestBody body = RequestBody.create(JSON_TYPE, jsonMsg);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("发送http请求异常，url=" + url + ", msg=" + jsonMsg, e);
        }
    }

    private String doGet(String url, Object msg) {
        // TODO Auto-generated method stub
        throw new RuntimeException("暂未实现");
    }

    private String getUrl(RemoteInfo remoteInfo) {
        String url = remoteInfo.getAdditionProps().getProperty("url");
        if(url == null){
            throw new RuntimeException("没有配置发送的url");
        }
        return "http://" + remoteInfo.getHost() + "/" + url;
    }
}
