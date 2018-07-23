package com.example.hua.framework.network;

/**
 * 网络请求结果发生错误(比如error_no==0)时的异常
 */
@Deprecated
public class NetResultErrorException extends Exception {
    private String error_info;
    private int error_no;
    private Throwable exception;

    public NetResultErrorException(String error_info, int error_no) {
        super(error_info);
        this.error_info = error_info;
        this.error_no = error_no;
    }

    public NetResultErrorException(String error_info, int error_no, Throwable exception) {
        super(exception.getMessage());
        this.error_info = error_info;
        this.error_no = error_no;
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public int getError_no() {
        return error_no;
    }

    public void setError_no(int error_no) {
        this.error_no = error_no;
    }
}