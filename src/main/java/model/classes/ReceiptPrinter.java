package model.classes;

import javafx.scene.control.Alert;

import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Represents a receipt printer
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
public class ReceiptPrinter implements Printable {
    /**
     * Transaction to be printed
     */
    private Transaction transaction;

    /**
     * Print method...
     *
     * @param g    Object that draws
     * @param pf   Format of the printout
     * @param page Page number
     * @return
     */
    public int print(final Graphics g, final PageFormat pf, final int page) {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        final Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        if (transaction != null) {
            String firmName = "";
            String phoneNumber = "";
            String businessId = "";
            String address = "";
            String postalCode = "";
            String city = "";
            String currency = "";
            String language = "";
            String country = "";
            String bonusAmount = "";
            final File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
            final Properties properties = new Properties();
            try (final FileReader reader = new FileReader(appConfigPath, StandardCharsets.UTF_8)) {
                properties.load(reader);
                firmName = properties.getProperty("firmName");
                phoneNumber = properties.getProperty("phoneNumber");
                businessId = properties.getProperty("businessId");
                address = properties.getProperty("address");
                postalCode = properties.getProperty("postalCode");
                city = properties.getProperty("city");
                currency = properties.getProperty("currency");
                language = properties.getProperty("language");
                country = properties.getProperty("country");
                bonusAmount = properties.getProperty("bonusAmount");
            } catch (final IOException e) {
                e.printStackTrace();
            }

            Locale locale = new Locale(language, country);
            ResourceBundle bundle = ResourceBundle.getBundle("TextResources", locale);


            final Order order = transaction.getOrder();
            int nameY = 260;
            int priceY = 260;
            final List<Product> productList = order.getProductList();
            g.drawString(firmName, 100, 100);
            g.drawString(address + ", " + postalCode + " " + city, 100, 120);
            g.drawString(phoneNumber + " " + businessId, 100, 140);
            if (transaction.getCustomer() != null) {
                g.drawString(bundle.getString("receipt") + ": " + order.getId() + " " + bundle.getString("bonusCustomerNumber") + ": " + transaction.getCustomer().getId(), 100, 160);
            } else {
                g.drawString(bundle.getString("receipt") + ": " + order.getId(), 100, 160);
            }
            g.drawString(bundle.getString("paymentmethod") + " " + transaction.getPaymentMethod(), 100, 180);
            g.drawString(bundle.getString("user") + ": " + transaction.getUser().getFullName(), 100, 200);
            g.drawString(String.valueOf(transaction.getTimestamp()), 100, 220);
            for (final Product product : productList) {
                String productName = product.getName();
                if (productName.length() > 20) {
                    productName = productName.substring(0, 20);
                    productName += "...";
                }
                g.drawString(productName, 100, nameY);
                nameY += 20;
            }
            for (final Product product : productList) {
                g.drawString(String.format("%.2f", (product.getPrice() / 100f)) + currency, 300, priceY);
                priceY += 20;
            }
            if (transaction.getCustomer() != null) {
                g.drawString("BONUS -" + bonusAmount + "%", 100, (nameY + 20));
            } else {
                g.drawString("BONUS: 0%", 100, (nameY + 20));
            }
            g.drawString(bundle.getString("price") + " " + String.format("%.2f", (order.getTotalPrice() / 100f)) + currency, 100,
                    (nameY + 40));
        }
        return PAGE_EXISTS;
    }

    /**
     * Helper function that is called from outside, this function calls for the
     * print method
     *
     * @param transaction transaction to print out
     */
    public boolean actionPerformed(final Transaction transaction) {
        this.transaction = transaction;
        final PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        final boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
                return true;
            } catch (final PrinterException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
