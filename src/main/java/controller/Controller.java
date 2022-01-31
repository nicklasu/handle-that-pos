package controller;

import model.classes.POSEngine;
import model.interfaces.IPOSEngine;
import view.MainApp;

/**
 * Ei käytössä
 */

public class Controller implements IController{
    private IPOSEngine engine;
    private MainApp ui;

    public Controller(MainApp ui) {
        this.engine = new POSEngine();
        this.ui = ui;
    }
}
