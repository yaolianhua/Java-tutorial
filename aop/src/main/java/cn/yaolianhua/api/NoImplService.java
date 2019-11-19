package cn.yaolianhua.api;

import org.springframework.stereotype.Service;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-12 22:19
 **/
@Service
public class NoImplService {
    public void noImpl(String value){
        System.out.println(this.getClass().getSimpleName() + " " + this.getClass().getDeclaredMethods()[0].getName());
    }
}
