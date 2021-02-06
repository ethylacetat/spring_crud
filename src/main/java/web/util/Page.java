package web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page<T> {
    private final List<T> paginatedContent;
    private final int pageNumber;
    private final boolean hasPrevPage;
    private final boolean hasNextPage;

    private final int rowCountByPage;

    public Page(List<T> paginatedContent,int pageNumber,
                boolean hasPrevPage, boolean hasNextPage, int rowCountByPage) {
        this.paginatedContent = Collections.unmodifiableList(new ArrayList<>(paginatedContent));
        this.pageNumber = pageNumber;
        this.hasPrevPage = hasPrevPage;
        this.hasNextPage = hasNextPage;
        this.rowCountByPage = rowCountByPage;
    }

    public List<T> getPaginatedContent() {
        return paginatedContent;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean getHasNextPage() {
        return hasNextPage;
    }

    public boolean getHasPrevPage() {
        return hasPrevPage;
    }

    public int prevPageNumber() {
        return pageNumber - 1;
    }

    public int nextPageNumber() {
        return pageNumber + 1;
    }

    public boolean isEmpty() {
        return paginatedContent.isEmpty();
    }

    public int getRowCountByPage() {
        return rowCountByPage;
    }

    public static <T> Page<T> emptyPage() {
        return emptyPage(0, false, false, 0);
    }

    public static <T> Page<T> emptyPage(int pageNumber, boolean hasPrevPage, boolean hasNextPage, int rowCountByPage) {
        return new Page<>(Collections.emptyList(), pageNumber, hasPrevPage, hasNextPage, rowCountByPage);
    }
}
