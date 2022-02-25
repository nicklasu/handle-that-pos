package model.classes;

import java.awt.*;
import java.awt.print.*;
import java.util.List;

public class ReceiptPrinter implements Printable {
    private Transaction transaction;

    public int print(Graphics g, PageFormat pf, int page) {

        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now we perform our rendering */
        if (transaction != null) {
            Order order = transaction.getOrder();
            int nameY = 260;
            int priceY = 260;
            List<Product> productList = order.getProductList();
            g.drawString("Tietokonevelhot Oy", 100, 100);
            g.drawString("Testikatu 1337, 00420 Helsinki", 100, 120);
            g.drawString("Puh. 0201234567 y-1234567-8", 100, 140);
            g.drawString("Kuitti: " + order.getId() + " Asiakas: " + transaction.getCustomer(), 100, 160);
            g.drawString("Myyjä: " + transaction.getUser().getFullName(), 100, 180);
            g.drawString(String.valueOf(transaction.getTimestamp()), 100, 200);
            for (Product product : productList) {
                g.drawString(product.getName(), 100, nameY);
                nameY += 20;
            }
            for (Product product : productList) {
                g.drawString((product.getPrice()) / 100f + "€", 400, priceY);
                priceY += 20;
            }
            g.drawString("Kokonaishinta: " + (order.getTotalPrice()) / 100f + "€", 100, (nameY + 40));
        }
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

    public void actionPerformed(Transaction transaction) {
        this.transaction = transaction;
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                /* The job did not successfully complete */
            }
        }
    }

}
