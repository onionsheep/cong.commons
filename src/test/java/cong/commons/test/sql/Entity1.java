/**
 * 创建 @ 2013年7月20日 下午3:35:02
 * 
 */
package cong.commons.test.sql;

import java.util.Date;

import cong.commons.sql.DBAI;
import cong.commons.sql.DBId;
import cong.commons.sql.DBTable;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
@DBTable("t_entity1")
public class Entity1 {
    @DBId
    @DBAI
    private int    id;
    private Date   t;
    private String s;

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Entity1 : {id:");
        builder.append(this.id);
        builder.append(", t:");
        builder.append(this.t);
        builder.append(", s:");
        builder.append(this.s);
        builder.append("}");
        return builder.toString();
    }

    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the t
     */
    public Date getT() {
        return this.t;
    }

    /**
     * @param t
     *            the t to set
     */
    public void setT(Date t) {
        this.t = t;
    }

    /**
     * @return the s
     */
    public String getS() {
        return this.s;
    }

    /**
     * @param s
     *            the s to set
     */
    public void setS(String s) {
        this.s = s;
    }

}
