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

    /**
     * Identifier of User related to the Profile
     */
    @Id
    @Column(name = "user_id", updatable = false, nullable = false, unique = true)
    private int id;

    /**
     * Avatar image as a Base64 encoded string
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * Default constructor
     */
    public Profile() {
    }

    /**
     * Constructor which takes user ID and image as a Base64 encoded string as paramteres
     * @param id int
     * @param avatar String
     */
    public Profile(final int id, final String avatar) {
        this.id = id;
        this.avatar = avatar;
    }

    /**
     * Returns user's ID related to profile
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Sets user ID
     * @param id int
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Returns profile image as a Base64 encoded String
     * @return String
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Sets profile image as a Base64 encoded String
     * @param avatar String
     */
    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }
}
