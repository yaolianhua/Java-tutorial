package cn.yaolianhua.extension.biz;

import org.springframework.stereotype.Service;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-20 09:36
 **/
@Service
public class ServiceExampleImpl2 implements ServiceExample2 {
    @Override
    public Object process(Object obj) {
        return obj;
    }
}
