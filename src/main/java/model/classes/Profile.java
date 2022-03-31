package model.classes;

import javax.persistence.*;

@Entity
@Table(name = "Profiili")
public class Profile {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false, unique = true)
    private int id;

    @Column(name="avatar")
    private String avatar;

    public Profile(){

    }

    public Profile(int id, String avatar) {
        this.id = id;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
