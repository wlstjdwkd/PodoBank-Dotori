package com.yongy.dotori.domain;


import com.yongy.dotori.global.common.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsgEntity<T> {
    private String status;
    private T data;


    public MsgEntity(String status, T data) {
        this.status = status;
        this.data = data;
    }
}
