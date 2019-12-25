package cn.yaolianhua.lock.repository;

import cn.yaolianhua.bean.DbStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-14 14:20
 **/
public interface DBStockRepository extends JpaRepository<DbStockEntity,Long> {

    Optional<DbStockEntity> findByDbStock(String stock);
}
