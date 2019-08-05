package cn.yaolianhua.dependencies.di.circular;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-07-31 20:34
 **/
public class CircularBean2 {
    private CircularBean1 bean1;

    public void setBean1(CircularBean1 bean1) {
        this.bean1 = bean1;
    }
}
