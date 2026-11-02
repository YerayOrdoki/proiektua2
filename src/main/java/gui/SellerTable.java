package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import domain.Sale;
import domain.Seller;
public final class SellerTable extends JFrame {
    private static final long serialVersionUID = 1L;
    public SellerTable(Seller seller) { this(seller, seller.getSales()); }
    public SellerTable(Seller seller, List<Sale> sales) {
        super(seller.getName() + "'s sales");
        JTable table = new JTable(new SellerAdapter(seller, sales));
        table.setAutoCreateRowSorter(true);
        table.setPreferredScrollableViewportSize(new Dimension(850, 180));
        add(new JScrollPane(table), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationByPlatform(true);
    }
}
