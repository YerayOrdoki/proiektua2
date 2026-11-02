package patterns.iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
/** Snapshot of list membership; cursor denotes the next element in either direction. */
public final class ListExtendedIterator<T> implements ExtendedIterator<T> {
    private final List<T> elements;
    private int cursor;
    public ListExtendedIterator(List<? extends T> elements) {
        this.elements = new ArrayList<T>(Objects.requireNonNull(elements, "elements"));
        goFirst();
    }
    @Override public boolean hasNext() { return cursor >= 0 && cursor < elements.size(); }
    @Override public boolean hasPrevious() { return cursor >= 0 && cursor < elements.size(); }
    @Override public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        return elements.get(cursor++);
    }
    @Override public T previous() {
        if (!hasPrevious()) throw new NoSuchElementException();
        return elements.get(cursor--);
    }
    @Override public void goFirst() { cursor = 0; }
    @Override public void goLast() { cursor = elements.size() - 1; }
    @Override public void remove() { throw new UnsupportedOperationException("Read-only snapshot"); }
}
