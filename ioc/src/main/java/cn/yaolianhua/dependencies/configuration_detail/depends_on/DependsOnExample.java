package cn.yaolianhua.dependencies.configuration_detail.depends_on;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-01 15:19
 **/
public class DependsOnExample {
    private ExampleBean exampleBean;

    public DependsOnExample() {
        System.out.println("DependsOnExample no param constructor");
    }

    public ExampleBean getExampleBean() {
        return exampleBean;
    }

    public void setExampleBean(ExampleBean exampleBean) {
        this.exampleBean = exampleBean;
    }
}
