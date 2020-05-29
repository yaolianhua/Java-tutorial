package cn.yaolianhua.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
public class IAuthCallback implements Serializable {
    private String code;
    private String state;
    private IAuthCallback(){}

    public IAuthCallback(String code, String state) {
        this.code = code;
        this.state = state;
    }
    public static IAuthCallbackBuilder builder(){
        return new IAuthCallbackBuilder();
    }
    @ToString
    public static class IAuthCallbackBuilder{
        private String code;
        private String state;
        private IAuthCallbackBuilder(){}
        public IAuthCallbackBuilder code(String code){
            this.code = code;
            return this;
        }
        public IAuthCallbackBuilder state(String state){
            this.state = state;
            return this;
        }
        public IAuthCallback build(){
            return new IAuthCallback(code,state);
        }
    }
}
