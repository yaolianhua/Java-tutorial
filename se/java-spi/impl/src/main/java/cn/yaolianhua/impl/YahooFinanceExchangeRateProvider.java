package cn.yaolianhua.impl;

import cn.yaolianhua.api.QuoteManager;
import cn.yaolianhua.spi.ExchangeRateProvider;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-01-03 14:59
 **/
public class YahooFinanceExchangeRateProvider implements ExchangeRateProvider {
    @Override
    public QuoteManager create() {
        return new YahooQuoteManagerImpl();
    }
}
