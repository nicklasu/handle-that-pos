package view;

import javafx.scene.layout.AnchorPane;

public class ViewLoader {
    public ViewLoader(final AnchorPane aPaneParent, final AnchorPane aPaneChild) {
        aPaneParent.getChildren().setAll(aPaneChild);
        aPaneChild.prefWidthProperty().bind(aPaneParent.widthProperty());
        aPaneChild.prefHeightProperty().bind(aPaneParent.heightProperty());
    }
}
