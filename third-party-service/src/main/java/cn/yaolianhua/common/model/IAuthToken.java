package cn.yaolianhua.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
public class IAuthToken implements Serializable {
        private String accessToken;
        private int expireIn;
        private String uid;
        private String openId;

    private IAuthToken() { }

    public IAuthToken(String accessToken, int expireIn, String uid, String openId) {
        this.accessToken = accessToken;
        this.expireIn = expireIn;
        this.uid = uid;
        this.openId = openId;
    }

    public static IAuthTokenBuilder builder(){
        return new IAuthTokenBuilder();
    }
    @ToString
    public  static class IAuthTokenBuilder{
        private String accessToken;
        private int expireIn;
        private String uid;
        private String openId;

        private IAuthTokenBuilder() { }
        public IAuthTokenBuilder accessToken(String accessToken){
            this.accessToken = accessToken;
            return this;
        }
        public IAuthTokenBuilder expireIn(int expireIn){
            this.expireIn = expireIn;
            return this;
        }
        public IAuthTokenBuilder uid(String uid){
            this.uid = uid;
            return this;
        }
        public IAuthTokenBuilder openId(String openId){
            this.openId = openId;
            return this;
        }
        public IAuthToken build(){
            return new IAuthToken(accessToken,expireIn,uid,openId);
        }
    }
}
