package view;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.classes.Product;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Controller for product-search-view.fxml.
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
public class ProductSearchController {
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
    @FXML
    private Button updateButton;

    private Pane wrapperPane;

    /**
     * Sets the mainApp to the one given in the parameter.
     * @param mainApp the mainApp to set
     */
    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        final List<Integer> verifiedPrivileges = this.mainApp.getEngine().getVerifiedPrivileges();
        final BooleanBinding booleanBind = input.textProperty().isEmpty();
        fetchBtn.disableProperty().bind(booleanBind);
        updateData();
        productTable.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2 && Collections.max(verifiedPrivileges) >= 2) {
                final int row = productTable.getSelectionModel().getSelectedIndex();
                System.out.println(productTable.getSelectionModel().getSelectedIndex());
                if (row >= 0) {
                    //final Product product = allProducts.get(row);
                    final Product product = productTable.getItems().get(row);
                    loadEditProductView(product);
                }
            }
        });

        input.textProperty()
                .addListener((observable, oldValue, newValue) -> filteredList.setPredicate(createPredicate(newValue)));
    }

    /**
     * Updates the data in the table.
     */
    @FXML
    private void updateData() {
        try {
            if (filteredList != null) {
                filteredList.removeAll();
            }
            if (productTable != null) {
                productTable.getItems().removeAll();
            }
            allProducts = this.mainApp.getEngine().productDao().getAllProducts();
            filteredList = new FilteredList<>(FXCollections.observableList(allProducts));

            productDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            productId.setCellValueFactory(new PropertyValueFactory<>("id"));
            productName.setCellValueFactory(new PropertyValueFactory<>("name"));
            productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            productStock.setCellValueFactory(new PropertyValueFactory<>("stock"));


                productTable.setItems(filteredList);

                productTable.setRowFactory(productTableView -> new TableRow<Product>() {
                    @Override
                    protected void updateItem(final Product product, final boolean empty) {
                        super.updateItem(product, empty);
                        if (product == null) {
                            setStyle("");
                        } else if (product.getStock() <= 0) {
                            setStyle("-fx-background-color: rgba(254, 97, 0, 0.5)");
                        } else if (product.getStock() <= 50) {
                            setStyle("-fx-background-color: rgba(255, 176, 0, 0.5)");
                        } else {
                            setStyle("-fx-background-color: rgba(100, 143, 255, 0.2)");
                        }
                    }
                });
            } catch( final Exception e){
                Notifications.create()
                        .owner(fetchBtn.getScene().getWindow())
                        .title(this.mainApp.getBundle().getString("errorString"))
                        .text(this.mainApp.getBundle().getString("errorGettingProducts"))
                        .position(Pos.TOP_RIGHT)
                        .showError();
                e.printStackTrace();
            }

        Image image = new Image(getClass().getResourceAsStream("/images/arrow-clockwise.png"), 20, 20, true, true);
        this.updateButton.setGraphic(new ImageView(image));
        }

    /**
     *  Handles searching for products
     * @param product product to be edited
     * @param searchText text to search for
     * @return
     */

        private boolean searchFindsProduct ( final Product product, final String searchText){
            return (product.getName().toLowerCase().contains(searchText.toLowerCase())) ||
                    (product.getId().toLowerCase().contains(searchText.toLowerCase()));
        }

    /**
     *  Creates a predicate to filter the products
     * @param searchText the text to search for
     * @return
     */

    private Predicate<Product> createPredicate ( final String searchText){
            return product -> {
                if (searchText == null || searchText.isEmpty())
                    return true;
                return searchFindsProduct(product, searchText);
            };
        }

    /**
     * Sets wrapper pane to the one given in the parameter.
     * @param wrapperPane the wrapper pane to set
     */
        public void setWrapperPane ( final Pane wrapperPane){
            this.wrapperPane = wrapperPane;
        }

    /**
     * Loads edit product view. This occurs when a product is double clicked on the table.
     * @param product The product to be edited.
     */
        private void loadEditProductView ( final Product product){
            wrapperPane.getChildren().clear();
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("products-view.fxml"));
            loader.setResources(this.mainApp.getBundle());
            Pane newLoadedPane = null;
            try {
                newLoadedPane = loader.load();
                final ProductManagementController view = loader.getController();
                view.setMainApp(mainApp);
                view.editProductPaneFillFields(product);
            } catch (final IOException e) {
                e.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        }

    }