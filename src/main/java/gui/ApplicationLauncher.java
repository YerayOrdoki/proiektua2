package gui;
import java.awt.Color;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import businessLogic.BLFactory;
import businessLogic.BLFacade;
import configuration.ConfigXML;
public class ApplicationLauncher {
    public static void main(String[] args) {
        ConfigXML c = ConfigXML.getInstance();
        Locale.setDefault(new Locale(c.getLocale()));
        BLFacade facade;
        try {
            // Potential DB/network work stays off the Swing event-dispatch thread.
            facade = new BLFactory().getBusinessLogicFactory(c.isBusinessLogicLocal());
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                MainGUI window = new MainGUI("");
                window.jLabelSelectOption.setText("Error: " + e.toString());
                window.jLabelSelectOption.setForeground(Color.RED);
                window.setVisible(true);
            });
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); }
            catch (Exception e) { System.err.println(e); }
            MainGUI.setBussinessLogic(facade);
            new MainGUI("").setVisible(true);
        });
    }
}
