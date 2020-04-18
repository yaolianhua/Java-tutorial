package cn.yaolianhua.spi;

import cn.yaolianhua.api.QuoteManager;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-01-03 14:43
 **/
public interface ExchangeRateProvider {
    QuoteManager create();
}
