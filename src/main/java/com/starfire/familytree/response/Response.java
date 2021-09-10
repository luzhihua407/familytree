package com.starfire.familytree.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class Response<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private boolean success = true;

    private int code = 200;

    @NonNull
    private String msg;

    private T result;

    private static  Response response;

    public static Response builder(){
        response = new Response();
        return response;
    }

    public static Response<String> failure(int code, String msg) {
        Response<String> response = new Response<String>(100, false, msg, null);
        return response;

    }

    public Response<T> failure() {
        Response<T> response = new Response<T>(100, false, "失败", null);
        return response;

    }

    // public static Response<String> success(String msg) {
    // Response<String> response = Response.of(200, true, msg, null);
    // return response;
    //
    // }

    // public static Response<Object> success(Object data) {
    // Response<Object> response = Response.of(200, true, "成功", data);
    // return response;
    //
    // }

//    public Response<T> success(T data) {
//        Response<T> response = new Response<T>(200, true, "成功", data);
//        return response;
//
//    }

//    public Response<T> success() {
//        Response<T> response = new Response<T>(200, true, "成功", null);
//        return response;
//
//    }

    public Response success(T data) {
        response.setResult(data);
        response.setCode(200);
        response.setSuccess(true);
        return response;
    }

    public Response success() {
        response.setCode(200);
        response.setSuccess(true);
        return response;
    }

    public Response(int code, boolean success, String msg, T result) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.result = result;
    }

    public Response() {
        super();
        response = new Response<T>(200, true, "成功", null);
    }

}
