package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import model.classes.Product;
import model.classes.User;

import java.util.List;

public class StatsView {

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

    private MainApp mainApp;
    private List<Product> allProducts;
    private List<User> allUsers;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        allProducts = this.mainApp.getEngine().productDao().getAllProducts();
        allUsers = this.mainApp.getEngine().userDAO().getAllUsers();
    }

    @FXML
    void initialize() {
        getProductStats();
        getUserStats();
    }

    boolean getProductStats(){
        XYChart.Series bestProducts = new XYChart.Series<>();
        XYChart.Series worstProducts = new XYChart.Series<>();

        //System.out.println(allProducts.size());

        bestProducts.getData().add(new XYChart.Data("asd",3));
        populateChart(bestSellingProductsChart, bestProducts);
        populateChart(worstSellingProductsChart,worstProducts);
        return true;
    }

    boolean getUserStats(){
        return true;
    }

    boolean populateChart(BarChart<?, ?> chart, XYChart.Series data){
        try{
            chart.getData().addAll(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }




}
