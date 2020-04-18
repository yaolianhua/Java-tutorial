package cn.yaolianhua.api;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-01-03 14:37
 **/
@Setter
@Getter
public class Quote {
    private String currency;
    private BigDecimal ask;
    private BigDecimal bid;
    private LocalDate date;
}
