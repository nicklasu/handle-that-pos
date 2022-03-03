package view;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.classes.Product;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class ProductSearchView {
    private MainApp mainApp;

    private List<Product> allProducts;

    private FilteredList<Product> filteredList;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> productDescription;

    @FXML
    private TableColumn<Product, String> productId;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Integer> productPrice;

    @FXML
    private TableColumn<Product, Integer> productStock;
    @FXML
    private Button fetchBtn;

    @FXML
    private TextField input;

    private Pane wrapperPane;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        BooleanBinding booleanBind = input.textProperty().isEmpty();
        fetchBtn.disableProperty().bind(booleanBind);
        updateData();
        productTable.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int row = productTable.getSelectionModel().getSelectedIndex();
                System.out.println(productTable.getSelectionModel().getSelectedIndex());
                Product product = allProducts.get(row);
                System.out.println(product);

                loadEditProductView(product);
            }
        });

        input.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(createPredicate(newValue))
        );
    }
    @FXML
    private void updateData(){
        try {
            if (!(filteredList == null)) {
                filteredList.removeAll();
            }
            if (!(productTable == null)) {
                productTable.getItems().removeAll();
            }
            allProducts = this.mainApp.getEngine().productDao().getAllProducts();
            filteredList = new FilteredList<>(FXCollections.observableList(allProducts));

            productDescription.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
            productId.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
            productName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
            productPrice.setCellValueFactory(new PropertyValueFactory<Product, Integer>("price"));
            productStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));

            productTable.setItems(filteredList);

            productTable.setRowFactory(productTableView -> new TableRow<Product>() {
                @Override
                protected void updateItem(Product product, boolean empty) {
                    super.updateItem(product, empty);
                    if (product == null) {
                        setStyle("");
                    } else if (product.getStock() <= 0) {
                        //setStyle("-fx-background-color: #ff3b3b;");
                        setStyle("-fx-background-color: rgba(255,59,59,0.5);");
                    } else if (product.getStock() <= 50) {
                        setStyle("-fx-background-color: rgba(255,216,103,0.5);");
                    } else {
                        //setStyle("");
                        setStyle("-fx-background-color: rgba(175,255,131,0.2);");
                    }
                }
            });
        } catch(Exception e){
            Notifications.create()
                    //.owner(fetchBtn.getScene().getWindow())
                    .title("Virhe")
                    .text("Tapahtui virhe tuotteiden hakemisessa!")
                    .position(Pos.TOP_RIGHT)
                    .showError();
            e.printStackTrace();
        }
    }

    private boolean searchFindsProduct(Product product, String searchText){
        return (product.getName().toLowerCase().contains(searchText.toLowerCase())) ||
                (product.getId().toLowerCase().contains(searchText.toLowerCase()));
    }

    private Predicate<Product> createPredicate(String searchText){
        return product -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsProduct(product, searchText);
        };
    }

    public void setWrapperPane(Pane wrapperPane) {
        this.wrapperPane = wrapperPane;
    }

    private void loadEditProductView(Product product) {
        wrapperPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("products-view.fxml"));
        Pane newLoadedPane = null;
        try {
            newLoadedPane = loader.load();
            ProductManagementView view = loader.getController();
            view.setMainApp(mainApp);
            view.editProductPaneFillFields(product);
        } catch (IOException e) {

        }
        wrapperPane.getChildren().add(newLoadedPane);
    }

}