package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
import java.net.URL;
import java.util.*;
import java.util.List;

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

    private User searchedUser;

    final private ObservableList<Transaction> items = FXCollections.observableArrayList();

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) searchUser();
        });
        activity.setDisable(true);
        this.searchedUser = this.mainApp.getEngine().getUser();
        fName.setText(this.searchedUser.getfName());
        lName.setText(this.searchedUser.getlName());
        setListViewItems(this.searchedUser);
        if (this.searchedUser.getActivity() == 1) {
            activity.setSelected(true);
        } else {
            activity.setSelected(false);
        }

        transactionListView.setOnMouseClicked(event -> {
            Transaction transaction = transactionListView.getSelectionModel().getSelectedItem();
            showTransactionDetails(transaction);
        });

        transactionListView.setCellFactory(param -> new ListCell<Transaction>() {
            @Override
            protected void updateItem(Transaction item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + ": " + item.getTimestamp());
                }
            }
        });

        Profile profile = this.mainApp.getEngine().profileDAO().getAvatar(this.searchedUser);
        if (profile != null) {
            insertImage(profile.getAvatar());
        }
    }

    @FXML
    private void searchUser() {
        try {
            System.out.println("searching");
            String username = searchField.getText();
            this.searchedUser = this.mainApp.getEngine().userDAO().getUser(username);
            if (this.searchedUser != null) {
                fName.setText(this.searchedUser.getfName());
                lName.setText(this.searchedUser.getlName());
                if (this.searchedUser.getActivity() == 1) {
                    activity.setSelected(true);
                } else {
                    activity.setSelected(false);
                }
                setListViewItems(this.searchedUser);
                Profile profile = this.mainApp.getEngine().profileDAO().getAvatar(this.searchedUser);
                if (profile != null) {
                    insertImage(profile.getAvatar());
                } else {

                    this.avatar.setImage(new Image(String.valueOf(getClass().getResource("/images/person.png"))));
                }
            } else {
                Notifications.create().owner(searchField.getScene().getWindow()).title("Virhe").text("Käyttäjänimeä ei löydy!").position(Pos.TOP_RIGHT).showError();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListViewItems(User user) {
        List<Transaction> transactions = this.mainApp.getEngine().transactionDAO().getTransactions(user);
        this.items.addAll(transactions);
        this.transactionListView.setItems(this.items);
    }

    private void showTransactionDetails(Transaction transaction) {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(Integer.toString(transaction.getId()));

        PaymentMethod paymentMethod;

        if (transaction.getPaymentMethodIndex() == 0) {
            paymentMethod = PaymentMethod.CASH;
        } else {
            paymentMethod = PaymentMethod.CARD;
        }

        String transactionInformation = "ID: " + transaction.getId() + "\n" + "Pvm ja kellonaika: " + transaction.getTimestamp() + "\n" + "Maksutapa: " + paymentMethod.name() + "\n" + "Myyjä: " + transaction.getUser().getFullName() + "\n" + "Maksupäätteen ID: " + transaction.getPos().getId() + "\n\n";

        String productsAndAmounts = "";
        Set<OrderProduct> ops = transaction.getOrder().getOrderProducts();
        for (OrderProduct op : ops) {
            productsAndAmounts += op.getProduct() + " x " + op.getAmount() + "\n";
        }

        dialog.setHeaderText(transactionInformation);
        dialog.setContentText(productsAndAmounts);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();

    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Image formats", "*.png");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.setTitle("Valitse kuva");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                if (bufferedImage != null) {


                    //resize bufferedImage
                    int newWidth = 600;
                    int newHeight = 600;
                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
                    g.dispose();



                    Image image = SwingFXUtils.toFXImage(resizedImage, null);
                    this.avatar.setImage(image);
                    String imageEncoded = encodeImage(resizedImage);
                    Profile profile = new Profile(this.searchedUser.getId(), imageEncoded);
                    this.mainApp.getEngine().profileDAO().saveAvatar(profile);
                } else {
                    Notifications.create().owner(avatar.getScene().getWindow()).title(this.mainApp.getBundle().getString("errorString")).text(this.mainApp.getBundle().getString("upload_image_error")).position(Pos.TOP_RIGHT).showError();

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Tapahtui virhe! Yritikö uploadata kuvaa väärässä formaatissa?");
            }
        }
    }


    private String encodeImage(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        String encodedImage = Base64.getEncoder().encodeToString(imageInByte);
        return encodedImage;
    }

    private void insertImage(String encodedImage) throws IOException {
        byte[] imageInByte2 = Base64.getDecoder().decode(encodedImage);  //decode image
        ByteArrayInputStream bais = new ByteArrayInputStream(imageInByte2);
        BufferedImage bufferedImage2 = ImageIO.read(bais);
        Image image2 = SwingFXUtils.toFXImage(bufferedImage2, null);
        System.out.println("Asetetaan kuva");
        this.avatar.setImage(image2);
    }

}
