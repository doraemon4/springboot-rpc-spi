package com.stephen.learning.blade.service;

import com.stephen.learning.blade.annotation.RpcService;
import com.stephen.learning.blade.dto.XmlRequest;
import com.stephen.learning.blade.dto.XmlResponse;

/**
 * @author wzy
 * @date 2017年11月13日 下午5:18:10
 */
@RpcService(protocol = "socket", host = "localhost:8091")
public interface SocketRpcService {

    XmlResponse call1030(XmlRequest request);

}
