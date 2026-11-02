package businessLogic;
import domain.Sale;
import patterns.iterator.ExtendedIterator;
import patterns.iterator.ListExtendedIterator;
/** Product shared by local logic and the remote client facade. */
public interface BLFacade extends RemoteBLFacade {
    /** The iterator is local state, never a SOAP return value. */
    default ExtendedIterator<Sale> getSalesIterator(String desc) {
        return new ListExtendedIterator<Sale>(getSales(desc));
    }
    default ExtendedIterator<Sale> getSalesIterator() {
        return getSalesIterator("");
    }
}
