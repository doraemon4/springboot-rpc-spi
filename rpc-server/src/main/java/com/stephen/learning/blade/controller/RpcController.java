package com.stephen.learning.blade.controller;

import com.alibaba.fastjson.JSON;
import com.stephen.learning.blade.dto.FooRequest;
import com.stephen.learning.blade.dto.FooResponse;
import com.stephen.learning.blade.dto.XmlRequest;
import com.stephen.learning.blade.dto.XmlResponse;
import com.stephen.learning.blade.service.HttpRpcService;
import com.stephen.learning.blade.service.NettyRpcService;
import com.stephen.learning.blade.service.SocketRpcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 16:51
 * @Version: 1.0
 */
@RestController
@RequestMapping("/demo")
public class RpcController {

    @Resource
    private SocketRpcService socketRpcService;
    @Resource
    private NettyRpcService nettyRpcService;
    @Resource
    private HttpRpcService httpRpcService;

    @GetMapping("/socket")
    public String testSocket(){
        XmlRequest request = new XmlRequest();
        request.setAge(12);
        request.setName("kvn");
        XmlResponse response = socketRpcService.call1030(request);
        return JSON.toJSONString(response);
    }

    @GetMapping("/http")
    public String testHttp(){
        FooRequest request = new FooRequest();
        FooResponse response = httpRpcService.call1030(request);
        return JSON.toJSONString(response);
    }

    @GetMapping("/netty")
    public String testNetty(){
        XmlRequest request = new XmlRequest();
        request.setAge(12);
        request.setName("kvn");
        XmlResponse response = nettyRpcService.call1030(request);
        return JSON.toJSONString(response);
    }
}
