package model.classes;

import java.awt.*;
import java.awt.print.*;
import java.util.List;

/**
 * Represents a receipt printer
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and Samu Luoma
 */
public class ReceiptPrinter implements Printable {
    /**
     * Transaction to be printed
     */
    private Transaction transaction;

    /**
     * Print method...
     * @param g Object that draws
     * @param pf Format of the printout
     * @param page Page number
     * @return
     */
    public int print(Graphics g, PageFormat pf, int page) {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        if (transaction != null) {
            Order order = transaction.getOrder();
            int nameY = 260;
            int priceY = 260;
            List<Product> productList = order.getProductList();
            g.drawString("Tietokonevelhot Oy", 100, 100);
            g.drawString("Testikatu 1337, 00420 Helsinki", 100, 120);
            g.drawString("Puh. 0201234567 y-1234567-8", 100, 140);
            if (transaction.getCustomer() != null) {
                g.drawString("Kuitti: " + order.getId() + " Asiakas: " + transaction.getCustomer().getId(), 100, 160);
            } else {
                g.drawString("Kuitti: " + order.getId(), 100, 160);
            }
            g.drawString("Maksutapa: " + transaction.getPaymentMethod(), 100, 180);
            g.drawString("Myyjä: " + transaction.getUser().getFullName(), 100, 200);
            g.drawString(String.valueOf(transaction.getTimestamp()), 100, 220);
            for (Product product : productList) {
                String productName = product.getName();
                if (productName.length() > 20) {
                    productName = productName.substring(0, 20);
                    productName += "...";
                }
                g.drawString(productName, 100, nameY);
                nameY += 20;
            }
            for (Product product : productList) {
                g.drawString(String.format("%.2f", (product.getPrice() / 100f)) + "€", 300, priceY);
                priceY += 20;
            }
            if (transaction.getCustomer() != null && transaction.getCustomer().getCustomerLevelIndex() == 1) {
                g.drawString("BONUS -5%", 100, (nameY + 20));
            } else {
                g.drawString("BONUS: 0%", 100, (nameY + 20));
            }
            g.drawString("Kokonaishinta: " + String.format("%.2f", (order.getTotalPrice() / 100f)) + "€", 100, (nameY + 40));
        }
        return PAGE_EXISTS;
    }

    /**
     * Helper function that is called from outside, this function calls for the print method
     * @param transaction transaction to print out
     */
    public void actionPerformed(Transaction transaction) {
        this.transaction = transaction;
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ignored) {
            }
        }
    }
}
