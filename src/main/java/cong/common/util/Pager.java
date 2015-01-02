package cong.common.util;

/**
 * Created by cong on 2014/10/24.
 */
public class Pager {
    public static long getTotalPage(long totalCount, int pageSize) {
        if (totalCount <= 0) {
            return 0;
        } else {
            return (totalCount - 1) / pageSize + 1;
        }

    }
}
