package com.mqz.nacos.gateway.config.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mqz
 * @description
 * @since 2020/4/20
 * @abount https://github.com/DemoMeng
 */
@Data
public class WithoutLoginException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -5395414930731458355L;

    private String msg;

    public WithoutLoginException(){
    }

    public WithoutLoginException(String msg){
        this.msg = msg;
    }
}
