package cn.yaolianhua.exception;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-01-03 14:41
 **/
public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException() {
        super();
    }

    public ProviderNotFoundException(String message) {
        super(message);
    }
}
