/**
 * 创建 @ 2013年7月23日 下午8:37:06
 * 
 */
package cong.commons.sql;

/**
 * 
 * 主键类型，ID（数字）,NAME（字符），PK（多个数字或字符作复合主键的时候）
 * 
 */
public enum KeyType {
    ID(1), NAME(2), NONE(0), PK(3);
    private final int value;

    KeyType() {
        this(0);
    }

    KeyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}