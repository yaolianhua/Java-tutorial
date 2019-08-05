package cn.yaolianhua.dependencies.configuration_detail.depends_on;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-01 15:20
 **/
public class ExampleBean {
    public ExampleBean() {
        System.out.println("ExampleBean no param constructor");
        initProcess();
    }

    public static void initProcess(){
        System.out.println("do process ...");
    }
}
