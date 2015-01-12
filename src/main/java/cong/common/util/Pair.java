/**
 * 2014年2月21日 下午8:00:59
 */
package cong.common.util;

/**
 * @author cong 刘聪 onion_sheep@163.com|onionsheep@gmail.com
 */
public class Pair<T1, T2> {

    public T1 v1;
    public T2 v2;

    public Pair(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T1 getV1() {
        return v1;
    }

    public void setV1(T1 v1) {
        this.v1 = v1;
    }

    public T2 getV2() {
        return v2;
    }

    public void setV2(T2 v2) {
        this.v2 = v2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (v1 != null ? !v1.equals(pair.v1) : pair.v1 != null) return false;
        if (v2 != null ? !v2.equals(pair.v2) : pair.v2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pair{");
        sb.append("v1=").append(v1);
        sb.append(", v2=").append(v2);
        sb.append('}');
        return sb.toString();
    }
}
