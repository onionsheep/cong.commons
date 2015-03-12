package cong.common.util.tuple;

/**
 * Created by cong on 2015/3/12.
 */
public class Tuple3<V1,V2,V3> {
    public V1 v1;
    public V2 v2;
    public V3 v3;

    public Tuple3(V1 v1, V2 v2, V3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;

        if (v1 != null ? !v1.equals(tuple3.v1) : tuple3.v1 != null) return false;
        if (v2 != null ? !v2.equals(tuple3.v2) : tuple3.v2 != null) return false;
        return !(v3 != null ? !v3.equals(tuple3.v3) : tuple3.v3 != null);

    }

    @Override
    public int hashCode() {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        result = 31 * result + (v3 != null ? v3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tuple3{");
        sb.append("v1=").append(v1);
        sb.append(", v2=").append(v2);
        sb.append(", v3=").append(v3);
        sb.append('}');
        return sb.toString();
    }

    public V1 getV1() {
        return v1;
    }

    public void setV1(V1 v1) {
        this.v1 = v1;
    }

    public V2 getV2() {
        return v2;
    }

    public void setV2(V2 v2) {
        this.v2 = v2;
    }

    public V3 getV3() {
        return v3;
    }

    public void setV3(V3 v3) {
        this.v3 = v3;
    }
}
