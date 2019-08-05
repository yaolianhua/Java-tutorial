package cn.yaolianhua.dependencies.di.circular;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-07-31 17:58
 **/
public class ExampleBean4 {
    private ExampleBean5 bean5;

    public ExampleBean4(ExampleBean5 bean5) {
        this.bean5 = bean5;
    }
}
