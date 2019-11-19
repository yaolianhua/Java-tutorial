package cn.yaolianhua.api;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-12 21:30
 **/
@org.springframework.stereotype.Service
public class MyService implements Service {
    @Override
    public String process(String value) {
        System.out.println(this.getClass().getSimpleName() + " " + this.getClass().getDeclaredMethods()[0].getName());
        return value;
    }

    @Override
    public void throwing() {

        throw new RuntimeException("runtime error");
    }
}
