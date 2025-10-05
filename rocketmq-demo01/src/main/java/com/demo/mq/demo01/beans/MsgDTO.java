package com.demo.mq.demo01.beans;


/**
 * @Author: lifei
 * @Date: 2025/10/5
 * @Description: 消息
 **/
public class MsgDTO {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MsgDTO{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
