package cn.yaolianhua.common.request;

import cn.yaolianhua.common.config.IAuthBase;
import cn.yaolianhua.common.config.IAuthBaseDefault;
import cn.yaolianhua.common.config.IAuthConfig;
import cn.yaolianhua.common.config.IAuthResponseStatus;
import cn.yaolianhua.common.model.IAuthCallback;
import cn.yaolianhua.common.model.IAuthResponse;
import cn.yaolianhua.common.model.IAuthToken;
import cn.yaolianhua.common.model.IAuthUser;
import cn.yaolianhua.common.utils.HttpUtil;
import cn.yaolianhua.common.utils.StringTools;
import cn.yaolianhua.common.utils.UrlBuilder;

import java.io.IOException;

public abstract class IAuthBaseRequest implements IAuthRequest{
    protected IAuthConfig config;
    protected IAuthBase authBase;
    public IAuthBaseRequest(IAuthConfig config,IAuthBase authBase){
        this.config = config;
        this.authBase = authBase;
    }

    protected abstract IAuthToken getAccessToken(IAuthCallback callback);

    protected abstract IAuthUser getUserInfo(IAuthToken authToken);

    @SuppressWarnings("unchecked")
    @Override
    public <T> IAuthResponse<T> login(IAuthCallback callback) {
        IAuthToken authToken = this.getAccessToken(callback);
        IAuthUser userInfo = this.getUserInfo(authToken);

        return (IAuthResponse<T>) IAuthResponse.builder()
                .code(IAuthResponseStatus.SUCCESS.getCode())
                .data(userInfo)
                .build();
    }

    @Override
    public String authorize(String state) {
        return UrlBuilder.baseUrl(authBase.authorize())
                .param("response_type","code")
                .param("client_id",config.getClientId())
                .param("redirect_uri",config.getRedirectUri())
                .param("state", getState(state))
                .build();
    }
    protected String accessTokenUrl(String code){
        return UrlBuilder.baseUrl(authBase.accessToken())
                .param("code",code)
                .param("client_id",config.getClientId())
                .param("client_secret",config.getClientSecret())
                .param("redirect_uri",config.getRedirectUri())
                .param("grant_type","authorization_code")
                .build();
    }
    protected String userInfoUrl(String token){
        return UrlBuilder.baseUrl(authBase.userInfo())
                .param("access_token",token)
                .build();
    }
    protected String getState(String state){
        if (StringTools.isEmpty(state))
            state = StringTools.getUUID();

        return state;
    }

    protected String doPostToken(String code){
        try {
            return HttpUtil.post(accessTokenUrl(code),"");
        } catch (IOException e) {
            return null;
        }
    }
    protected String doGetUser(String token){
        try {
            return HttpUtil.get(userInfoUrl(token));
        } catch (IOException e) {
            return null;
        }
    }
}
