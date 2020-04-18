package cn.yaolianhua;

import cn.yaolianhua.exception.ProviderNotFoundException;
import cn.yaolianhua.spi.ExchangeRateProvider;

import java.util.*;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-01-03 14:44
 **/
public final class ExchangeRate {
    private static final String DEFAULT_PROVIDER = "cn.yaolianhua.spi.YahooFinanceExchangeRateProvider";

    //All providers
    public static List<ExchangeRateProvider> providers() {
        List<ExchangeRateProvider> services = new ArrayList<>();
        ServiceLoader<ExchangeRateProvider> loader = ServiceLoader.load(ExchangeRateProvider.class);
        Iterator<ExchangeRateProvider> iterator = loader.iterator();
        while (iterator.hasNext()) {
            services.add(iterator.next());
        }
//        loader.forEach(services::add);
        return services;
    }

    //Default provider
    public static ExchangeRateProvider provider() {
        return provider(DEFAULT_PROVIDER);
    }

    //provider by name
    public static ExchangeRateProvider provider(String providerName) {
        ServiceLoader<ExchangeRateProvider> loader = ServiceLoader.load(ExchangeRateProvider.class);
        for (ExchangeRateProvider provider : loader) {
            if (Objects.equals(provider.getClass().getName(), providerName)) {
                return provider;
            }
        }
        throw new ProviderNotFoundException("Exchange Rate provider " + providerName + " not found");
    }
}
