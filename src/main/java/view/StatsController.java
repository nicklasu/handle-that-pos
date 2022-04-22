package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import model.classes.OrderProduct;
import model.classes.Product;
import model.classes.Transaction;
import model.classes.User;

import java.util.*;

public class StatsController {

    @FXML
    private BarChart<?, ?> bestSellingProductsChart;

    @FXML
    private CategoryAxis bestSellingProductsX;

    @FXML
    private NumberAxis bestSellingProductsY;

    @FXML
    private BarChart<?, ?> bestSellingUsersChart;

    @FXML
    private CategoryAxis bestSellingUsersX;

    @FXML
    private NumberAxis bestSellingUsersY;

    @FXML
    private Tab usersChart;

    @FXML
    private BarChart<?, ?> worstSellingProductsChart;

    @FXML
    private CategoryAxis worstSellingProductsX;

    @FXML
    private NumberAxis worstSellingProductsY;

    @FXML
    private BarChart<?, ?> worstSellingUsersChart;

    @FXML
    private CategoryAxis worstSellingUsersX;

    @FXML
    private NumberAxis worstSellingUsersY;

    @FXML
    private ProgressIndicator progressIndicator;

    private MainApp mainApp;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        System.out.println(this.mainApp.getEngine().productDao().getAllProducts().size());
        createCharts();
    }

    void createCharts(){
        progressIndicator.setVisible(true);
        final Thread thread = new Thread(() -> {
            try {
                List<User> allUsers = this.mainApp.getEngine().userDAO().getAllUsers();
                List<Product> allProducts = this.mainApp.getEngine().productDao().getAllProducts();
                List<UserWithSales> users = new ArrayList<>();
                List<ProductWithSales> products = new ArrayList<>();

                for (Product p : allProducts) {
                    products.add(new ProductWithSales(p.getId(),p.getName(), 0));
                }
                
                for (User u : allUsers) {
                    int totalSales = 0;
                    List<Transaction> transactions = this.mainApp.getEngine().transactionDAO().getTransactions(u);
                    for (Transaction t: transactions) {
                        totalSales += t.getOrder().getTotalPriceWithoutBonuses();
                        List<Product> productsInTransaction = t.getOrder().getProductList();

                        final Set<OrderProduct> ops = t.getOrder().getOrderProducts();
                        for (final OrderProduct op : ops) {
                            //sb.append(op.getProduct() + " x " + op.getAmount() + "\n");
                            productsInTransaction.add(op.getProduct());
                            // op.getAmount() saa yhden transactionin yhden tuotteen määrän
                        }




                        for (Product p : productsInTransaction) {
                            for (ProductWithSales pws : products) {
                                if(p.getId().equals(pws.getProductID())){
                                    pws.increment();
                                }
                            }
                        }
                    }
                    users.add(new UserWithSales(u.getUsername(),totalSales));
                }

                Collections.sort(products);
                Collections.sort(users);

                List<ProductWithSales> productsTop5 = new ArrayList<>();
                List<UserWithSales> usersTop5 = new ArrayList<>();
                List<ProductWithSales> productsBottom5 = new ArrayList<>();
                List<UserWithSales> usersBottom5 = new ArrayList<>();

                int maxUsers = Math.min(allUsers.size(), 5);
                int maxProducts = Math.min(allProducts.size(), 5);

                for (int i = 0; i < maxUsers; i++) {
                    usersTop5.add(users.get(i));
                    usersBottom5.add(users.get(maxUsers-i));
                }
                for (int i = 0; i < maxProducts; i++) {
                    productsTop5.add(products.get(i));
                    productsBottom5.add(products.get(maxProducts-i));
                }



                Platform.runLater(() -> {
                    makeDataSet(productsTop5,usersTop5,productsBottom5,usersBottom5);
                    progressIndicator.setVisible(false);
                });
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    void makeDataSet(List<ProductWithSales> productsTop5, List<UserWithSales> usersTop5, List<ProductWithSales> productsBottom5, List<UserWithSales> usersBottom5){
        XYChart.Series<String, Integer> dataSet1 = new XYChart.Series<>();
        XYChart.Series<String, Integer> dataSet2 = new XYChart.Series<>();
        XYChart.Series<String, Integer> dataSet3 = new XYChart.Series<>();
        XYChart.Series<String, Integer> dataSet4 = new XYChart.Series<>();

        for (int i = 0; i < productsTop5.size(); i++) {
            dataSet1.getData().add(new XYChart.Data(productsTop5.get(i).getProductID(),productsTop5.get(i).getSold()));
            dataSet2.getData().add(new XYChart.Data(productsBottom5.get(i).getProductID(),productsBottom5.get(i).getSold()));
        }
        for (int i = 0; i < usersTop5.size(); i++) {
            dataSet3.getData().add(new XYChart.Data(usersTop5.get(i).getUserName(),usersTop5.get(i).getSales()));
            dataSet4.getData().add(new XYChart.Data(usersTop5.get(i).getUserName(),usersBottom5.get(i).getSales()));
        }

        populateChart(bestSellingProductsChart, dataSet1);
        populateChart(worstSellingProductsChart,dataSet2);
        populateChart(bestSellingUsersChart,dataSet3);
        populateChart(worstSellingUsersChart,dataSet4);
    }


    boolean populateChart(BarChart<?, ?> chart, XYChart.Series data){
        try{
            chart.getData().addAll(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    

    private class ProductWithSales implements Comparable<ProductWithSales>{
        private String productID;
        private String productName;
        private int sold;
        public ProductWithSales(String x, String y, int z){
            productID = x;
            productName = y;
            sold = z;
        }
        public String getProductID() {
            return productID;
        }

        public void increment(){
            sold++;
        }

        public int getSold() {
            return sold;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
        @Override
        public int compareTo(ProductWithSales pws){
            return pws.sold - this.sold;
        }
    }
    private class UserWithSales implements Comparable<UserWithSales>{
        private String userName;
        private int sales;
        public UserWithSales(String x, int y){
            userName = x;
            sales = y;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getSales() {
            return sales;
        }

        public void setSales(int sales) {
            this.sales = sales;
        }
        @Override
        public int compareTo(UserWithSales uws){
            return uws.sales - this.sales;
        }
    }
}
