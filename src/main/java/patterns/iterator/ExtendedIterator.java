package patterns.iterator;
import java.util.Iterator;
/** Bidirectional traversal. goFirst/goLast reset the next element to return. */
public interface ExtendedIterator<T> extends Iterator<T> {
    T previous();
    boolean hasPrevious();
    void goFirst();
    void goLast();
}
