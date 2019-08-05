package cn.yaolianhua.dependencies.annotation;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-03 11:15
 **/
public class ExampleBeanImpl2 implements ExampleBeanPrimaryInterface {
    private String string;

    public ExampleBeanImpl2(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
