package cn.yaolianhua.proxy;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-07 15:03
 **/
public class SimplePojo implements Pojo {
    @Override
    public void foo() {
        System.out.println("SimplePojo foo()");
        this.bar();
    }

    @Override
    public void bar() {
        System.out.println("SimplePojo bar()");
    }
}
