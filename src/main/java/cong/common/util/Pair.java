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

}
