package cong.common.util;

/**
 * Created by cong on 2014/11/25.
 */
public class PageParam {
    private Integer pageNum;
    private Integer pageSize;

    public PageParam(Integer page, Integer pageSize) {
        this.pageNum = page;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PageParam{");
        sb.append("pageNum=").append(pageNum);
        sb.append(", pageSize=").append(pageSize);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageParam pageParam = (PageParam) o;

        if (pageNum != null ? !pageNum.equals(pageParam.pageNum) : pageParam.pageNum != null) return false;
        if (pageSize != null ? !pageSize.equals(pageParam.pageSize) : pageParam.pageSize != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pageNum != null ? pageNum.hashCode() : 0;
        result = 31 * result + (pageSize != null ? pageSize.hashCode() : 0);
        return result;
    }

    @Deprecated
    public Integer getPage() {
        return pageNum;
    }

    @Deprecated
    public void setPage(Integer page) {
        this.pageNum = page;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer page) {
        this.pageNum = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
