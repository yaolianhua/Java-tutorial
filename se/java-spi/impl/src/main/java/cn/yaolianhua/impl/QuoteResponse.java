package cn.yaolianhua.impl;

import cn.yaolianhua.api.Quote;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-01-03 14:56
 **/
@Getter
@Setter
public class QuoteResponse {
    private List<Quote> result;
    private String error;
}
