package cn.yaolianhua.common.request;

import cn.yaolianhua.common.config.IAuthBaseDefault;
import cn.yaolianhua.common.config.IAuthConfig;
import cn.yaolianhua.common.model.GithubUser;
import cn.yaolianhua.common.model.IAuthCallback;
import cn.yaolianhua.common.model.IAuthToken;
import cn.yaolianhua.common.model.IAuthUser;
import cn.yaolianhua.common.utils.HttpUtil;
import cn.yaolianhua.common.utils.StringTools;

import java.util.Map;

import static cn.yaolianhua.common.utils.JsonUtil.json2Bean;

public class GithubRequest extends IAuthBaseRequest{
    public GithubRequest(IAuthConfig config) {
        super(config, IAuthBaseDefault.GITHUB);
    }

    @Override
    protected IAuthToken getAccessToken(IAuthCallback callback) {
        final String token = doPostToken(callback.getCode());
        final Map<String, String> map = HttpUtil.parseUrlParam(token);
        return IAuthToken.builder()
                .accessToken(map.get("access_token"))
                .expireIn(Integer.parseInt(StringTools.isEmpty(map.get("expire_in")) ? "0" : map.get("expire_in")))
                .openId(map.get("open_id"))
                .build();
    }

    @Override
    protected IAuthUser getUserInfo(IAuthToken authToken) {
        final String user = doGetUser(authToken.getAccessToken());
        return json2Bean(user,GithubUser.class);
    }
}
