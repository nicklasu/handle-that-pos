package view;

import javafx.scene.layout.AnchorPane;

/**
 * Loads views.
 * Called in OptionsController.
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class ViewLoader {
    public ViewLoader(final AnchorPane aPaneParent, final AnchorPane aPaneChild) {
        aPaneParent.getChildren().setAll(aPaneChild);
        aPaneChild.prefWidthProperty().bind(aPaneParent.widthProperty());
        aPaneChild.prefHeightProperty().bind(aPaneParent.heightProperty());
    }
}
