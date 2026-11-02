package patterns;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.junit.Assume;
import org.junit.Test;
import domain.*;
import gui.SellerTable;
import patterns.demo.PatternsDemo;
import patterns.iterator.ListExtendedIterator;
/** Screenshots of real Swing windows rendered by Java, not mock images. */
public class PatternsScreenshotTest {
    private void capture(JFrame frame, String name) throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> frame.setVisible(true));
            Robot robot = new Robot(); robot.waitForIdle(); robot.delay(600);
            Rectangle[] bounds = new Rectangle[1];
            SwingUtilities.invokeAndWait(() -> bounds[0] = frame.getBounds());
            BufferedImage image = robot.createScreenCapture(bounds[0]);
            assertTrue(ImageIO.write(image, "png", new File("doc/patterns/" + name + ".png")));
        } finally { SwingUtilities.invokeAndWait(() -> frame.dispose()); }
    }
    @Test public void captureExecutedExamples() throws Exception {
        Assume.assumeFalse("Run with xvfb-run to capture real windows", GraphicsEnvironment.isHeadless());
        Files.createDirectories(new File("doc/patterns").toPath());
        Seller seller = new Seller("seller1@example.test", "Demo Seller", "unused");
        Sale a = seller.addSale("Bicycle", "City bicycle", 1, 120f, new Date(0), null);
        Sale b = seller.addSale("Monitor", "24 inch", 0, 85f, new Date(86400000L), null);
        Sale c = seller.addSale("Keyboard", "Mechanical", 1, 35f, new Date(172800000L), null);
        a.setSaleNumber(1); b.setSaleNumber(2); c.setSaleNumber(3);
        String output = PatternsDemo.describe(new ListExtendedIterator<Sale>(seller.getSales()));
        assertTrue(output.indexOf("3;Keyboard") < output.indexOf("1;Bicycle"));
        Files.write(new File("doc/patterns/iterator-output.txt").toPath(), output.getBytes(StandardCharsets.UTF_8));
        JFrame[] windows = new JFrame[2];
        SwingUtilities.invokeAndWait(() -> {
            windows[0] = new SellerTable(seller);
            windows[0].setLocation(30,30);
            JFrame console = new JFrame("Iterator - executed example (deterministic fixture)");
            JTextArea text = new JTextArea(output);
            text.setEditable(false); text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));
            console.add(new JScrollPane(text)); console.setSize(730,360); console.setLocation(30,300);
            windows[1] = console;
        });
        capture(windows[0], "adapter");
        capture(windows[1], "iterator");
    }
}
