package cn.yaolianhua.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-14 13:36
 **/
@Entity
@Table(name = "db_lock", schema = "distributed")
public class DbLockEntity {

    private long id;
    private String dbLock;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "db_lock", nullable = false, length = 32)
    public String getDbLock() {
        return dbLock;
    }

    public void setDbLock(String dbLock) {
        this.dbLock = dbLock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbLockEntity that = (DbLockEntity) o;
        return id == that.id &&
                Objects.equals(dbLock, that.dbLock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dbLock);
    }
}
