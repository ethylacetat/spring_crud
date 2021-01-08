package web.util;

import java.util.Collection;

public class JPAUtil {

    public static void initialize(Collection<?> lazyFetchedCollection) {
        if (lazyFetchedCollection != null) {
            lazyFetchedCollection.iterator().hasNext();
        }
    }
}
