package cn.yaolianhua.dependencies.javaBase;


/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 10:46
 **/
public class B implements C{
    public void doSomething(){
        System.out.println("B doSomething()");
    }

    public void doProcess() {
        System.out.println("B doProcess");
    }

    public void init(){
        System.out.println(" B init invoked");
    }

    public void destroy(){
        System.out.println("B destroy invoked");
    }
}
