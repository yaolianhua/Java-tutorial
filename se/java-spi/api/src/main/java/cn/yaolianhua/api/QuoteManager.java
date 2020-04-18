package cn.yaolianhua.api;

import java.time.LocalDate;
import java.util.List;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-01-03 14:39
 **/
public interface QuoteManager {
    List<Quote> getQuotes(String baseCurrency, LocalDate date);
}
