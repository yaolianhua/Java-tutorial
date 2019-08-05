package cn.yaolianhua.dependencies.di.circular;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-07-31 20:34
 **/
public class CircularBean1 {
    private CircularBean2 bean2;

    public void setBean2(CircularBean2 bean2) {
        this.bean2 = bean2;
    }
}
