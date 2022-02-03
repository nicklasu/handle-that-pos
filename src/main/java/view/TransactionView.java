package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import model.classes.Product;

import java.io.IOException;
import java.util.ArrayList;

public class TransactionView {
    private MainApp mainApp;
    @FXML
    private AnchorPane transactionAnchorPane;
    @FXML
    private ListView scanListView;
    private ObservableList<Product> items = FXCollections.observableArrayList();
    @FXML
    public void loadMainView() throws IOException {
        //new ViewLoader(transactionAnchorPane, FXMLLoader.load(getClass().getResource("main-view.fxml")));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        new ViewLoader(transactionAnchorPane, fxmlLoader.load());
        ((MainView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        if (this.mainApp.getEngine().getTransaction() != null) {
            ArrayList<Product> products = this.mainApp.getEngine().getTransaction().getOrder().getProductList();
            items.addAll(products);
        }
        scanListView.setItems(items);
    }
}
