package com.starfire.familytree.exception;

import lombok.Data;

@Data
public class FamilyException extends RuntimeException {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

    public FamilyException() {
        super();
    }

    public FamilyException(String message) {
        super(message);
        this.msg=message;
    }

    public FamilyException(Integer code,String message){
        this.msg=message;
        this.code=code;
    }
    public FamilyException(String message, Throwable cause) {
        super(message, cause);
        this.msg=message;
    }

}
