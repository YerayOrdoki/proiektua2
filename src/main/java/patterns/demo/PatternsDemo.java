package patterns.demo;
import businessLogic.BLFacade;
import businessLogic.BLFactory;
import configuration.ConfigXML;
import domain.Sale;
import domain.Seller;
import gui.SellerTable;
import java.util.List;
import javax.swing.SwingUtilities;
import patterns.iterator.ExtendedIterator;
/** Real application demo: optional seller email and search text, no hard-coded credentials. */
public final class PatternsDemo {
    public static String describe(ExtendedIterator<Sale> it) {
        StringBuilder out = new StringBuilder("FROM LAST TO FIRST\n");
        it.goLast();
        while (it.hasPrevious()) out.append(it.previous()).append('\n');
        out.append("\nFROM FIRST TO LAST\n");
        it.goFirst();
        while (it.hasNext()) out.append(it.next()).append('\n');
        return out.toString();
    }
    public static void main(String[] args) {
        BLFacade bl = new BLFactory().getBusinessLogicFactory(ConfigXML.getInstance().isBusinessLogicLocal());
        System.out.print(describe(bl.getSalesIterator(args.length > 1 ? args[1] : "")));
        if (args.length > 0) {
            Seller seller = bl.getSellerByEmail(args[0]);
            if (seller == null) throw new IllegalArgumentException("Seller not found: " + args[0]);
            List<Sale> sales = bl.getSalesBySeller(seller.getEmail());
            SwingUtilities.invokeLater(() -> new SellerTable(seller, sales).setVisible(true));
        }
    }
}
