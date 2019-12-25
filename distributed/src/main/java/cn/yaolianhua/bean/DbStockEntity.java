package cn.yaolianhua.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-14 14:19
 **/
@Entity
@Table(name = "db_stock", schema = "distributed")
public class DbStockEntity {
    private long id;
    private String dbStock;
    private int dbStockNum;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "db_stock", nullable = false, length = 32)
    public String getDbStock() {
        return dbStock;
    }

    public void setDbStock(String dbStock) {
        this.dbStock = dbStock;
    }

    @Basic
    @Column(name = "db_stock_num", nullable = false)
    public int getDbStockNum() {
        return dbStockNum;
    }

    public void setDbStockNum(int dbStockNum) {
        this.dbStockNum = dbStockNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbStockEntity that = (DbStockEntity) o;
        return id == that.id &&
                dbStockNum == that.dbStockNum &&
                Objects.equals(dbStock, that.dbStock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dbStock, dbStockNum);
    }
}
