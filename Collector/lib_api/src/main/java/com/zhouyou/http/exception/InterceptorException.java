/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-7-31 下午2:02
 *  FileName:InterceptorException.java
 */

package com.zhouyou.http.exception;

/**
 * Created by ZY on 2019/7/31
 * DESC: class InterceptorException
 */
public class InterceptorException extends Error {
    private int code;
    private String message;


    public InterceptorException(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "InterceptorException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
