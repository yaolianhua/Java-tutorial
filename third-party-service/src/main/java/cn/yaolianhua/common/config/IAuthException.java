package cn.yaolianhua.common.config;

public class IAuthException extends RuntimeException{

    private int errCode;
    private String errMsg;

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public IAuthException(int errCode, String errMsg){
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    public IAuthException(String errMsg){
        this(IAuthResponseStatus.FAILURE.getCode(),errMsg);
    }

    public IAuthException(IAuthResponseStatus status){
        this(status.getCode(),status.getMsg());
    }
}
