package cn.yaolianhua.rpc.framework;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-26 16:15
 **/
@Getter
@Setter
@ToString
public class RpcRequest implements Serializable {
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] params;
    private String className;
}
