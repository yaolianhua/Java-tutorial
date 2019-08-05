package cn.yaolianhua.dependencies.customBean;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-02 11:08
 **/
public class LifecycleCallbacksExampleWithXml {
    private String string;

    public LifecycleCallbacksExampleWithXml(String string) {
        this.string = string;
    }
    public void init(){
        System.out.println("custom init invoked");
    }
    public void destroy(){
        System.out.println("custome destroy invoked");
    }
}
