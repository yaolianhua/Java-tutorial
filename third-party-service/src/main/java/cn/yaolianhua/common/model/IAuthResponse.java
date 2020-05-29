package cn.yaolianhua.common.model;

import cn.yaolianhua.common.config.IAuthResponseStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
public class IAuthResponse<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    private IAuthResponse() {
    }

    public boolean ok(){
        return this.code == IAuthResponseStatus.SUCCESS.getCode();
    }

    public IAuthResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> IAuthResponseBuilder<T> builder(){
        return new IAuthResponseBuilder<>();
    }
    @ToString
    public static class IAuthResponseBuilder<T>{
        private int code;
        private String msg;
        private T data;

        private IAuthResponseBuilder() {
        }
        public IAuthResponseBuilder<T> code(int code){
            this.code = code;
            return this;
        }
        public IAuthResponseBuilder<T> msg(String msg){
            this.msg = msg;
            return this;
        }
        public IAuthResponseBuilder<T> data(T data){
            this.data = data;
            return this;
        }
        public IAuthResponse<T> build(){
            return new IAuthResponse<>(code,msg,data);
        }
    }

}
