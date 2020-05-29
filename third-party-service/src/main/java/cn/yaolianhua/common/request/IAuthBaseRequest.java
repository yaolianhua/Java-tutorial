package cn.yaolianhua.common.request;

import cn.yaolianhua.common.config.IAuthBase;
import cn.yaolianhua.common.config.IAuthConfig;

public abstract class IAuthBaseRequest implements IAuthRequest{
    protected IAuthConfig config;
    protected IAuthBase authBase;
    public IAuthBaseRequest(IAuthConfig config,IAuthBase authBase){
        this.config = config;
        this.authBase = authBase;
    }


}
