package cn.yaolianhua.dependencies.annotation;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-03 11:15
 **/
public class ExampleBeanImpl3 implements ExampleBeanQualifierInterface {
    private String string;

    public ExampleBeanImpl3(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
