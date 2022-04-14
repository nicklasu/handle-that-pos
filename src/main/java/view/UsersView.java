package view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.classes.*;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

/**
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class UsersView {
    private MainApp mainApp;
    @FXML
    private TextField searchField;
    @FXML
    private Text fName;
    @FXML
    private Text lName;
    @FXML
    private CheckBox activity;
    @FXML
    private ListView<Transaction> transactionListView;
    @FXML
    private ImageView avatar;
    @FXML
    private ProgressIndicator progressIndicator;
    private User searchedUser;
    @FXML
    private Button searchButton;

    private final ObservableList<Transaction> items = FXCollections.observableArrayList();
    private DateFormat dateFormat;

    public void setMainApp(final MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        final List<Integer> verifiedPrivileges = this.mainApp.getEngine().getVerifiedPrivileges();
        // if user has user level privileges or less disable the search field
        if (Collections.max(verifiedPrivileges) < 2) {
            this.searchField.setDisable(true);
            this.searchButton.setDisable(true);
        }
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                searchUser();
        });
        activity.setDisable(true);
        this.searchedUser = this.mainApp.getEngine().getUser();
        fName.setText(this.searchedUser.getfName());
        lName.setText(this.searchedUser.getlName());
        setListViewItems(this.searchedUser);
        final boolean act = this.searchedUser.getActivity() == 1;
        activity.setSelected(act);

        transactionListView.setOnMouseClicked(event -> {
            final Transaction transaction = transactionListView.getSelectionModel().getSelectedItem();
            showTransactionDetails(transaction);
        });

        transactionListView.setCellFactory(param -> new ListCell<Transaction>() {
            @Override
            protected void updateItem(final Transaction item, final boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + ": " + dateFormat.format(item.getTimestamp()));
                }
            }
        });

        final Profile profile = this.mainApp.getEngine().profileDAO().getAvatar(this.searchedUser);
        if (profile != null) {
            insertImage(profile.getAvatar());
        }

        this.dateFormat = DateFormat.getDateTimeInstance();
    }

    @FXML
    private void searchUser() {
        try {
            System.out.println("searching");
            final String username = searchField.getText();
            this.searchedUser = this.mainApp.getEngine().userDAO().getUser(username);
            if (this.searchedUser != null) {
                fName.setText(this.searchedUser.getfName());
                lName.setText(this.searchedUser.getlName());
                final boolean act = this.searchedUser.getActivity() == 1;
                activity.setSelected(act);
                setListViewItems(this.searchedUser);
                final Profile profile = this.mainApp.getEngine().profileDAO().getAvatar(this.searchedUser);
                if (profile != null) {
                    insertImage(profile.getAvatar());
                } else {

                    this.avatar.setImage(new Image(String.valueOf(getClass().getResource("/images/person.png"))));
                }
            } else {
                Notifications.create().owner(searchField.getScene().getWindow()).title("Virhe")
                        .text("Käyttäjänimeä ei löydy!").position(Pos.TOP_RIGHT).showError();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void setListViewItems(final User user) {
        // new thread
        progressIndicator.setVisible(true);
        final Thread thread = new Thread(() -> {
            try {
                final List<Transaction> transactions = this.mainApp.getEngine().transactionDAO().getTransactions(user);
                Platform.runLater(() -> {
                    items.clear();
                    items.addAll(transactions);
                    transactionListView.setItems(items);
                    progressIndicator.setVisible(false);

                });
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }

    private void showTransactionDetails(final Transaction transaction) {

        final Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(Integer.toString(transaction.getId()));

        PaymentMethod paymentMethod;

        if (transaction.getPaymentMethodIndex() == 0) {
            paymentMethod = PaymentMethod.CASH;
        } else {
            paymentMethod = PaymentMethod.CARD;
        }

        final String transactionInformation = "ID: " + transaction.getId() + "\n" + "Pvm ja kellonaika: "
                + dateFormat.format(transaction.getTimestamp()) + "\n" + "Maksutapa: " + paymentMethod.name() + "\n" + "Myyjä: "
                + transaction.getUser().getFullName() + "\n" + "Maksupäätteen ID: " + transaction.getPos().getId()
                + "\n\n";

        String productsAndAmounts = "";
        final Set<OrderProduct> ops = transaction.getOrder().getOrderProducts();
        StringBuilder sb = new StringBuilder();
        for (final OrderProduct op : ops) {
            sb.append(op.getProduct() + " x " + op.getAmount() + "\n");
        }
        productsAndAmounts = sb.toString();

        dialog.setHeaderText(transactionInformation);
        dialog.setContentText(productsAndAmounts);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();

    }

    @FXML
    private void uploadImage() {
        final FileChooser fileChooser = new FileChooser();
        final FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Image formats", "*.png");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.setTitle("Valitse kuva");
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                final BufferedImage bufferedImage = ImageIO.read(file);
                if (bufferedImage != null) {

                    // resize bufferedImage
                    final int newWidth = 600;
                    final int newHeight = 600;
                    final BufferedImage resizedImage = new BufferedImage(newWidth, newHeight,
                            BufferedImage.TYPE_INT_ARGB);
                    final Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
                    g.dispose();

                    final Image image = SwingFXUtils.toFXImage(resizedImage, null);
                    this.avatar.setImage(image);
                    final String imageEncoded = encodeImage(resizedImage);
                    final Profile profile = new Profile(this.searchedUser.getId(), imageEncoded);
                    this.mainApp.getEngine().profileDAO().saveAvatar(profile);
                } else {
                    Notifications.create().owner(avatar.getScene().getWindow())
                            .title(this.mainApp.getBundle().getString("errorString"))
                            .text(this.mainApp.getBundle().getString("upload_image_error")).position(Pos.TOP_RIGHT)
                            .showError();

                }
            } catch (final IOException e) {
                e.printStackTrace();
                System.out.println("Tapahtui virhe! Yritikö uploadata kuvaa väärässä formaatissa?");
            }
        }
    }

    private String encodeImage(final BufferedImage bufferedImage) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.flush();
        final byte[] imageInByte = baos.toByteArray();
        baos.close();
        return Base64.getEncoder().encodeToString(imageInByte);
    }

    private void insertImage(final String encodedImage) throws IOException {
        final byte[] imageInByte2 = Base64.getDecoder().decode(encodedImage); // decode image
        final ByteArrayInputStream bais = new ByteArrayInputStream(imageInByte2);
        final BufferedImage bufferedImage2 = ImageIO.read(bais);
        final Image image2 = SwingFXUtils.toFXImage(bufferedImage2, null);
        System.out.println("Asetetaan kuva");
        this.avatar.setImage(image2);
    }

}
