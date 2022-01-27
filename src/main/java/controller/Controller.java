package controller;

import model.classes.POSEngine;
import model.interfaces.IPOSEngine;
import view.MainGUI;

public class Controller implements IController{
    private IPOSEngine engine;
    private MainGUI ui;

    public Controller(MainGUI ui) {
        this.engine = new POSEngine();
        this.ui = ui;
    }
}
