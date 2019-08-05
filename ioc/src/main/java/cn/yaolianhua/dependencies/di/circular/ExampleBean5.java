package cn.yaolianhua.dependencies.di.circular;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-07-31 17:58
 **/
public class ExampleBean5 {
    private ExampleBean4 bean4;

    public ExampleBean5(ExampleBean4 bean4) {
        this.bean4 = bean4;
    }
}
