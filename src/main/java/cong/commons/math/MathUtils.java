package cong.commons.math;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MathUtils {

    /**
     * 求List中最大的第K个数，不止限于数值，一切可以比较的对象都可以。 <br/>
     * 比如：list = [1,3,5,7,9,2,4,6,8,10], k = 5, 返回6。 <br/>
     * 算法的最坏情况下的时间复杂度为O(n*k)，最好为O(n),其中n是列表中元素的个数<br/>
     * 算法的空间复杂度为O(k)，使用了一个辅助列表。<br/>
     * 该方法适合在k比较小的时候使用
     * 
     * @param list
     *            存放待求最大第K个数的List
     * @param k
     *            K
     * @return 最大的第K个数
     */
    public static <T extends Comparable<T>> T topK(List<T> list, int k) {
        int len = list.size();
        if (len < k) {
            return null;
        }
        if (len == k) {
            T min = list.get(0);
            for (T t : list) {
                if (min.compareTo(t) > 0) {
                    min = t;
                }
            }
            return min;
        }
        List<T> topk = new LinkedList<T>();
        topk.addAll(list.subList(0, k));
        Collections.sort(topk);
        for (int i = k; i < len; i++) {
            T t = list.get(i);
            if (t.compareTo(topk.get(0)) > 0) {
                int j;
                for (j = 1; j < k; j++) {
                    if (t.compareTo(topk.get(j)) < 0) {
                        break;
                    }
                }
                topk.add(j, t);
                topk.remove(0);
            }
        }
        return topk.get(0);
    }

    /**
     * 求方差
     * 
     * @param list
     *            样本
     * @param avg
     *            平均值
     * @return 方差
     */
    public static double variance(Iterable<? extends Number> list, Number avg) {
        double davg = avg.doubleValue();
        double var = 0;
        for (Number n : list) {
            double d = n.doubleValue();
            var += (d - davg) * (d - davg);
        }
        return var;
    }

    /**
     * 求方差
     * 
     * @param list
     *            样本
     * @return 方差
     */
    public static double variance(Iterable<? extends Number> list) {
        int count = 0;
        double var = 0;
        double sum = 0;
        double dsum = 0;
        for (Number n : list) {
            double d = n.doubleValue();
            sum += d;
            dsum += d * d;
            count += 1;
        }
        var = dsum - sum * sum / count;
        return var;
    }

}
