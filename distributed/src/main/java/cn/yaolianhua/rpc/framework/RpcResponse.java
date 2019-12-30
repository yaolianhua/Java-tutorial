package cn.yaolianhua.rpc.framework;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-26 17:22
 **/
@Getter
@Setter
public class RpcResponse implements Serializable {
    private Object result;
    private Throwable error;
}
