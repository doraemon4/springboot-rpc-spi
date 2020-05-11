package com.stephen.learning.blade.dto;

import com.stephen.learning.blade.annotation.DecodeBy;


@DecodeBy(DecodeBy.JSON)
public class FooResponse {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "FooResponse [code=" + code + ", msg=" + msg + "]";
    }

}
