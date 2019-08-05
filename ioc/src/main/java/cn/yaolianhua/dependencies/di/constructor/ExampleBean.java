package cn.yaolianhua.dependencies.di.constructor;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-07-31 17:00
 **/
public class ExampleBean {
    private int num;
    private String str;

    public ExampleBean(int num, String str) {
        this.num = num;
        this.str = str;
    }
}
