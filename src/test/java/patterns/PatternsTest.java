package patterns;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.*;
import org.junit.Test;
import businessLogic.*;
import dataAccess.DataAccess;
import domain.*;
import gui.SellerAdapter;
import patterns.iterator.*;
public class PatternsTest {
    @Test public void reverseThenForwardAndBounds() {
        ExtendedIterator<Integer> it = new ListExtendedIterator<Integer>(Arrays.asList(1,2,3));
        it.goLast();
        List<Integer> backwards = new ArrayList<Integer>();
        while(it.hasPrevious()) backwards.add(it.previous());
        assertEquals(Arrays.asList(3,2,1), backwards);
        assertThrows(NoSuchElementException.class, () -> it.previous());
        it.goFirst();
        List<Integer> forwards = new ArrayList<Integer>();
        while(it.hasNext()) forwards.add(it.next());
        assertEquals(Arrays.asList(1,2,3), forwards);
        assertThrows(NoSuchElementException.class, () -> it.next());
        assertThrows(UnsupportedOperationException.class, () -> it.remove());
    }
    @Test public void emptyAndSingleton() {
        ExtendedIterator<Integer> empty = new ListExtendedIterator<Integer>(Collections.emptyList());
        assertFalse(empty.hasNext());
        assertFalse(empty.hasPrevious());
        empty.goLast();
        assertFalse(empty.hasPrevious());
        assertThrows(NoSuchElementException.class, () -> empty.previous());
        ExtendedIterator<Integer> one = new ListExtendedIterator<Integer>(Arrays.asList(7));
        assertEquals(Integer.valueOf(7), one.next());
        assertFalse(one.hasNext());
        one.goLast();
        assertEquals(Integer.valueOf(7), one.previous());
        assertFalse(one.hasPrevious());
    }
    @Test public void snapshotAndDirectionChange() {
        List<Integer> input = new ArrayList<Integer>(Arrays.asList(1,2,3));
        ExtendedIterator<Integer> it = new ListExtendedIterator<Integer>(input);
        input.clear();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.previous());
        assertEquals(Integer.valueOf(1), it.previous());
        it.goLast();
        assertEquals(Integer.valueOf(3), it.next());
    }
    @Test public void factoryUsesInjectedCreatorsWithoutDatabase() {
        BLFacade local = mock(BLFacade.class), remote = mock(BLFacade.class);
        BLFactory.Creator lc = mock(BLFactory.Creator.class), rc = mock(BLFactory.Creator.class);
        when(lc.create()).thenReturn(local); when(rc.create()).thenReturn(remote);
        BLFactory factory = new BLFactory(lc,rc);
        assertSame(local, factory.getBusinessLogicFactory(true));
        verify(rc, never()).create();
        assertSame(remote, factory.getBusinessLogicFactory(false));
        verify(lc).create(); verify(rc).create();
    }
    @Test public void localFacadeUsesExistingSearch() {
        DataAccess db = mock(DataAccess.class);
        Sale sale = new Sale();
        when(db.getSales("bike")).thenReturn(Arrays.asList(sale));
        BLFacade bl = new BLFacadeImplementation(db);
        assertSame(sale, bl.getSalesIterator("bike").next());
        org.mockito.InOrder order = inOrder(db);
        order.verify(db).open(); order.verify(db).getSales("bike"); order.verify(db).close();
    }
    @Test public void remoteIteratorStaysOnClient() {
        RemoteBLFacade soap = mock(RemoteBLFacade.class);
        Sale sale = new Sale();
        when(soap.getSales("bike")).thenReturn(Arrays.asList(sale));
        when(soap.getSales("")).thenReturn(Arrays.asList(sale));
        BLFacade bl = new RemoteFacade(soap);
        assertSame(sale, bl.getSalesIterator("bike").next());
        assertSame(sale, bl.getSalesIterator().next());
        verify(soap).getSales("bike"); verify(soap).getSales("");
        verifyNoMoreInteractions(soap);
    }
    @Test public void sellerAdapterMapsValuesAndTypes() {
        Seller seller = new Seller("demo@example.test", "Demo", "unused");
        Date date = new Date(0);
        Sale sale = seller.addSale("Bike", "Demo", 1, 42.5f, date, null);
        sale.setSaleNumber(9);
        SellerAdapter adapter = new SellerAdapter(seller);
        assertEquals(1, adapter.getRowCount()); assertEquals(6, adapter.getColumnCount());
        Object[] expected = {9, "Bike", "Demo", 1, 42.5f, date};
        for(int column=0; column<expected.length; column++) {
            assertEquals(expected[column], adapter.getValueAt(0,column));
            assertTrue(adapter.getColumnClass(column).isInstance(expected[column]));
            assertFalse(adapter.isCellEditable(0,column));
        }
        seller.addSell(new Sale());
        assertEquals(1, adapter.getRowCount());
        assertThrows(IndexOutOfBoundsException.class, () -> adapter.getValueAt(0,6));
    }
    @Test public void emptyAndRemoteSellerAdapter() {
        Seller seller = new Seller("demo@example.test", "Demo", "unused");
        SellerAdapter empty = new SellerAdapter(seller);
        assertEquals(0, empty.getRowCount()); assertEquals(Float.class, empty.getColumnClass(4));
        SellerAdapter remote = new SellerAdapter(seller, Arrays.asList(new Sale()));
        assertEquals(1, remote.getRowCount());
        assertNull(remote.getValueAt(0,0));
    }
}
