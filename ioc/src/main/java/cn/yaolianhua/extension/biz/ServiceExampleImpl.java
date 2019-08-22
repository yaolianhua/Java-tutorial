package cn.yaolianhua.extension.biz;

import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-16 15:37
 **/
@Service
public class ServiceExampleImpl implements ServiceExample ,ServiceExample3{
    @Override
    public String process(String val,String val2) {
        return "ServiceExampleImpl process("+ val +" "+ val2+")";
    }

    @Override
    public String process3(String val) {
        return "ServiceExampleImpl process3("+val+")";
    }
}
