package com.stephen.learning.blade.scan;

import com.stephen.learning.blade.proxy.RpcProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 13:48
 * @Version: 1.0
 */
public class RpcFactoryBean<T> implements FactoryBean<T> {
    private Class<T> rpcInterface;

    @Override
    public T getObject() throws Exception {
        return RpcProxyFactory.newInstance(rpcInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return rpcInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<T> getRpcInterface() {
        return rpcInterface;
    }

    public void setRpcInterface(Class<T> rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

}
