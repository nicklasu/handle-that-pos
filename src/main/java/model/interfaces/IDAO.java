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
    public boolean createEntry(DatabaseObject v);

    /**
     * lukee yksittäisen tallennetun objektin syötetyn id avulla
     *
     * @param id luettavan objektin id
     * @return Palauttaa koko tallennetun objektin
     */
    public DatabaseObject readEntry(int id);

    /**
     * Lukee koko tietokannan sisällön
     *
     * @return palauttaa koko tietokannan sisällön object array -muodossa
     */
    public DatabaseObject[] readAll();

    /**
     * Päivittää tietokantaan tallennetun objektin
     *
     * @param v päivitettävä objekti uusilla arvoilla
     * @return palauttaa boolean onnistumisesta riippuen
     */
    public boolean updateEntry(DatabaseObject v);

    /**
     * Poistaa tietokantaan tallennetun objektin
     *
     * @param id poistettavan objektin id
     * @return palauttaa boolean onnistumiseta riippuen
     */
    public boolean deleteEntry(int id);
}