package gui;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.swing.table.AbstractTableModel;
import domain.Sale;
import domain.Seller;
/** Object Adapter: Seller/Sale -> Swing TableModel. */
public final class SellerAdapter extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNS = {"Sale", "Title", "Description", "Status", "Price", "Publication date"};
    private static final Class<?>[] TYPES = {Integer.class, String.class, String.class, Integer.class, Float.class, Date.class};
    private final List<Sale> sales;
    public SellerAdapter(Seller seller) {
        this(Objects.requireNonNull(seller, "seller"), seller.getSales());
    }
    /** Remote sellers omit sales in JAXB; pass getSalesBySeller(email) explicitly. */
    public SellerAdapter(Seller seller, List<Sale> sales) {
        Objects.requireNonNull(seller, "seller");
        this.sales = new ArrayList<Sale>(Objects.requireNonNull(sales, "sales"));
    }
    @Override public int getRowCount() { return sales.size(); }
    @Override public int getColumnCount() { return COLUMNS.length; }
    @Override public String getColumnName(int column) { return COLUMNS[column]; }
    @Override public Class<?> getColumnClass(int column) { return TYPES[column]; }
    @Override public boolean isCellEditable(int row, int column) { return false; }
    @Override public Object getValueAt(int row, int column) {
        Sale s = sales.get(row);
        switch (column) {
        case 0: return s.getSaleNumber();
        case 1: return s.getTitle();
        case 2: return s.getDescription();
        case 3: return s.getStatus();
        case 4: return s.getPrice();
        case 5: return s.getPublicationDate();
        default: throw new IndexOutOfBoundsException("column: " + column);
        }
    }
}
