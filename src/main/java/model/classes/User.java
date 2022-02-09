package model.classes;

import javax.persistence.*;

@Entity
@Table(name = "Käyttäjä")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private int id;

    @Column(name="Käyttäjätunnus", unique = true)
    private String username;

    @Column(name="Salasana")
    private String password;

    @Column(name="Etunimi")
    private String fName;
    @Column(name="Sukunimi")
    private String lName;
    @Column(name="Aktiivisuus")
    private int activity;

    public User() {
    }

    public User(String fName, String lName, String username, String password, int activity){
        this.fName = fName;
        this.lName = lName;
        this.username = username;
        this.password = password;
        this.activity = activity;
    }

    public User(int id, String fName, String lName, String username, String password, int activity){
        this(fName, lName, username, password, activity);
        this.id = id;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String name) {
        this.fName = fName;
    }
    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;

    }

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

    public String getFullName() {
        return fName + " " + lName;
    }
}
