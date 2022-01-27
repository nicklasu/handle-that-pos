package model.interfaces;
import model.classes.DatabaseObject;

/**
 * Interface-luokka tietokantaan tallennettavien objectien käsittelyä varten,
 * käsittää perus "CRUD" metodit
 *
 */
public interface IDAO {

    /**
     * Tallentaa syötetyn objektin tietokantaan
     *
     * @param v Tallennettava objekti
     * @return palauttaa boolean onnistumisesta riippuen
     */
    boolean createEntry(DatabaseObject v);

    /**
     * lukee yksittäisen tallennetun objektin syötetyn id avulla
     *
     * @param id luettavan objektin id
     * @return Palauttaa koko tallennetun objektin
     */
    DatabaseObject readEntry(int id);

    /**
     * Lukee koko tietokannan sisällön
     *
     * @return palauttaa koko tietokannan sisällön object array -muodossa
     */
    DatabaseObject[] readAll();

    /**
     * Päivittää tietokantaan tallennetun objektin
     *
     * @param v päivitettävä objekti uusilla arvoilla
     * @return palauttaa boolean onnistumisesta riippuen
     */
    boolean updateEntry(DatabaseObject v);

    /**
     * Poistaa tietokantaan tallennetun objektin
     *
     * @param id poistettavan objektin id
     * @return palauttaa boolean onnistumiseta riippuen
     */
    boolean deleteEntry(int id);
}