package cn.yaolianhua.common.request;

import cn.yaolianhua.common.config.IAuthException;
import cn.yaolianhua.common.config.IAuthResponseStatus;
import cn.yaolianhua.common.model.IAuthCallback;
import cn.yaolianhua.common.model.IAuthResponse;

public interface IAuthRequest {

    default String authorize(String state){
        throw new IAuthException(IAuthResponseStatus.NOT_IMPLEMENTED);
    }
    default <T> IAuthResponse<T> login(IAuthCallback callback){
        throw new IAuthException(IAuthResponseStatus.NOT_IMPLEMENTED);
    }
}
