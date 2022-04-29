package model.classes;

import javax.persistence.*;

/**
 * Represents a user/employee
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Entity
@Table(name = "Käyttäjä")
public class User {

    /**
     * Unique identifier of the User
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private int id;

    /**
     * Username for user
     */
    @Column(name = "Käyttäjätunnus", unique = true)
    private String username;

    /**
     * Password for user
     */
    @Column(name = "Salasana")
    private String password;

    /**
     * First name of user
     */
    @Column(name = "Etunimi")
    private String fName;

    /**
     * Last name of user
     */
    @Column(name = "Sukunimi")
    private String lName;

    /**
     * Activity of user, used to differentiate inactive users when browsing all the
     * users
     */
    @Column(name = "Aktiivisuus")
    private int activity;

    /**
     * Empty costructor for hibernate
     */
    public User() {
    }

    /**
     * Constructor for users
     * 
     * @param fName    first name
     * @param lName    last name
     * @param username username to log in with
     * @param password password to log in with
     * @param activity whether user is active of not
     */
    public User(final String fName, final String lName, final String username, final String password,
            final int activity) {
        this.fName = fName;
        this.lName = lName;
        this.username = username;
        this.password = password;
        this.activity = activity;
    }

    /**
     * Secondary constructor allowing id to be set
     * 
     * @param id
     * @param fName
     * @param lName
     * @param username
     * @param password
     * @param activity
     */
    public User(final int id, final String fName, final String lName, final String username, final String password,
            final int activity) {
        this(fName, lName, username, password, activity);
        this.id = id;
    }

    /**
     * @return identifier of user
     */
    public int getId() {
        return id;
    }

    /**
     * @param id identifier of user
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return username of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username username of user
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password password of user
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return first name of user
     */
    public String getfName() {
        return fName;
    }

    /**
     * @param fName first name of user
     */
    public void setfName(final String fName) {
        this.fName = fName;
    }

    /**
     * @return last name of user
     */
    public String getlName() {
        return lName;
    }

    /**
     * @param lName last name of user
     */
    public void setlName(final String lName) {
        this.lName = lName;
    }

    /**
     * @return activity of user
     */
    public int getActivity() {
        return activity;
    }

    /**
     * @param activity activity of user
     */
    public void setActivity(final int activity) {
        this.activity = activity;
    }

    /**
     * @return User information in string format
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", activity=" + activity +
                '}';
    }

    /**
     * @return full name of user
     */
    public String getFullName() {
        return fName + " " + lName;
    }
}
