package cn.yaolianhua.dependencies.di.example;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-07-31 17:39
 **/
public class ExampleBean3 {

    private SimpleBean3 simpleBean3;
    private String string;

    private ExampleBean3(SimpleBean3 simpleBean3, String string) {
        this.simpleBean3 = simpleBean3;
        this.string = string;
    }

    public static ExampleBean3 getInstance(SimpleBean3 simpleBean3,String string){
        ExampleBean3 bean3 = new ExampleBean3(simpleBean3, string);
        //some other operations
        return bean3;
    }
}
