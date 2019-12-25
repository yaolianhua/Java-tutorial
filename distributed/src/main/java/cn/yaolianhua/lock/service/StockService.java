package cn.yaolianhua.lock.service;

import cn.yaolianhua.bean.DbStockEntity;
import cn.yaolianhua.lock.repository.DBStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-14 14:11
 **/
@Service
public class StockService {

    @Autowired
    DBStockRepository stockRepository;

    public boolean stockReduce(String stock){

        Optional<DbStockEntity> stockEntity = stockRepository.findByDbStock(stock);
        boolean present = stockEntity.isPresent();
        if (!present)
            return false;
        DbStockEntity entity = stockEntity.get();
        if (entity.getDbStockNum() < 1){
            System.out.println("Stock Reduction Failed,Stock Number is zero");
            return false;
        }
        entity.setDbStockNum(entity.getDbStockNum() - 1);
        stockRepository.save(entity);

        return true;
    }

}
