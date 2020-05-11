package com.stephen.learning.blade.core;

import java.util.Properties;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 14:56
 * @Version: 1.0
 */
public class RemoteInfo {
    private String host;
    private Properties additionProps;

    public RemoteInfo(String host, Properties additionProps) {
        super();
        this.host = host;
        this.additionProps = additionProps;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public Properties getAdditionProps() {
        return additionProps;
    }
    public void setAdditionProps(Properties additionProps) {
        this.additionProps = additionProps;
    }
}
