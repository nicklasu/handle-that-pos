package view;

/**
 * Used for running the program.
 * Disables operating system scaling.
 * Necessary for the way the program's graphical layout is designed.
 * Calls MainApp after this.
 * @author Nicklas Sundell
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");
        MainApp.main(args);
    }
}
