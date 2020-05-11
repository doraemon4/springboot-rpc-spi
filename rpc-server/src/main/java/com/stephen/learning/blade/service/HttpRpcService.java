package com.stephen.learning.blade.service;


import com.stephen.learning.blade.annotation.Addition;
import com.stephen.learning.blade.annotation.RpcService;
import com.stephen.learning.blade.dto.FooRequest;
import com.stephen.learning.blade.dto.FooResponse;

/**
 * @author wzy
 * @date 2017年11月13日 下午5:18:10
 */
@RpcService(protocol = "http", host = "172.16.21.28")
public interface HttpRpcService {
	
	@Addition("type=post&url=mockjsdata/35/refund/api/query/querySettlement")
	FooResponse call1030(FooRequest request);

}
