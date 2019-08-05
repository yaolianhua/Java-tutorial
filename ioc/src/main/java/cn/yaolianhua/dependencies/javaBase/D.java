package cn.yaolianhua.dependencies.javaBase;


/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 10:46
 **/
public class D{
    private C c;
    public void doSomething(){
        System.out.println("D doSomething()");
    }

    public D(C c) {
        this.c = c;
    }

    public void init(){
        System.out.println(" D init invoked");
    }
    public void destroy(){
        System.out.println("D destroy invoked");
    }
}
