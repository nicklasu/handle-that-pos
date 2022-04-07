package model.classes;

import javax.persistence.*;

/**
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Entity
@Table(name = "Profiili")
public class Profile {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false, unique = true)
    private int id;

    @Column(name = "avatar")
    private String avatar;

    public Profile() {

    }

    public Profile(final int id, final String avatar) {
        this.id = id;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }
}
