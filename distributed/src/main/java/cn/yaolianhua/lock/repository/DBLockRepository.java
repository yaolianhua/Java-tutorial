package cn.yaolianhua.lock.repository;

import cn.yaolianhua.bean.DbLockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-14 13:41
 **/
public interface DBLockRepository extends JpaRepository<DbLockEntity,Long> {

    Optional<DbLockEntity> findByDbLock(String dbLock);

    @Transactional
    void deleteByDbLock(String dbLock);
}
