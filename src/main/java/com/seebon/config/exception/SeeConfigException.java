package com.seebon.config.exception;

/**
 * initial exceptions
 *
 * @author xfz
 * @Date 2018年06月06日
 * @Version
 */
public class SeeConfigException extends RuntimeException {
    private static final long serialVersionUID = 1390696866045892051L;

    private String message;

    public SeeConfigException() {
        super();
    }

    public SeeConfigException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
