package cn.yaolianhua.common.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class IAuthConfig {
    private String clientId;//appKey
    private String clientSecret;//appSecret
    private String redirectUri;//redirect uri

    private IAuthConfig() { }

    public IAuthConfig(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
    public static AuthConfigBuilder builder(){
        return new AuthConfigBuilder();
    }
    @ToString
    public static class AuthConfigBuilder{
        private String clientId;
        private String clientSecret;
        private String redirectUri;

        private AuthConfigBuilder() {}
        public AuthConfigBuilder clientId(String clientId){
            this.clientId = clientId;
            return this;
        }
        public AuthConfigBuilder clientSecret(String clientSecret){
            this.clientSecret = clientSecret;
            return this;
        }
        public AuthConfigBuilder redirectUri(String redirectUri){
            this.redirectUri = redirectUri;
            return this;
        }

        public IAuthConfig build(){
            return new IAuthConfig(clientId,clientSecret,redirectUri);
        }
    }
}
