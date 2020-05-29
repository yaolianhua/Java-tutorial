package cn.yaolianhua.common.config;

public enum IAuthBaseDefault implements IAuthBase {

    GITHUB{
        @Override
        public String authorize() {
            return "https://github.com/login/oauth/authorize";
        }

        @Override
        public String accessToken() {
            return "https://github.com/login/oauth/access_token";
        }

        @Override
        public String userInfo() { return "https://api.github.com/user"; }
    }
}
