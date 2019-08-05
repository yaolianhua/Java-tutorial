package cn.yaolianhua.dependencies.di.constructor;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-07-31 16:22
 **/
public class SimpleService {
    private final SimpleBean simpleBean;
    public SimpleService(SimpleBean simpleBean) {
        this.simpleBean = simpleBean;
    }
}
