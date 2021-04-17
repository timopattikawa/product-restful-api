package com.kamoro.product.api.model;

public class WebResponse<T> {

    private Integer Code;

    private String status;

    private T data;

    public WebResponse(Integer code, String status, T data) {
        Code = code;
        this.status = status;
        this.data = data;
    }

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
