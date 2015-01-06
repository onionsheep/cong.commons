package cong.common.util;

/**
 * Created by cong on 2015/1/6.
 */
public class KV<K, V> {
    private K key;
    private V value;

    public KV(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KV{");
        sb.append("key=").append(key);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KV kv = (KV) o;

        if (key != null ? !key.equals(kv.key) : kv.key != null) return false;
        if (value != null ? !value.equals(kv.value) : kv.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}