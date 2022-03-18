package model.classes;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Käyttöoikeus")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private int id;

    @Column(name = "Oikeusalku")
    private Date privilegeStart;

    @Column(name = "Oikeusloppu")
    private Date privilegeEnd;

    @Column(name = "KäyttäjätasoID")
    private int privilegeLevelIndex;

    @Transient
    private PrivilegeLevel privilegeLevel;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "KäyttäjäID", referencedColumnName = "id")
    private User user;


    public Privilege() {

    }

    public Privilege(User user, Date privilegeStart, Date privilegeEnd, PrivilegeLevel privilegeLevel) {
        this.user = user;
        this.privilegeLevel = privilegeLevel; // Oletuksena myyjän oikeudet
        this.privilegeLevelIndex = privilegeLevel.ordinal();
        this.privilegeStart = privilegeStart;
        this.privilegeEnd = privilegeEnd;
    }

    public Privilege(Date privilegeStart, Date privilegeEnd, PrivilegeLevel privilegeLevel) {
        this.privilegeLevel = privilegeLevel; // Oletuksena myyjän oikeudet
        this.privilegeLevelIndex = privilegeLevel.ordinal();
        this.privilegeStart = privilegeStart;
        this.privilegeEnd = privilegeEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPrivilegeStart() {
        return privilegeStart;
    }

    public void setPrivilegeStart(Date privilegeStart) {
        this.privilegeStart = privilegeStart;
    }

    public Date getPrivilegeEnd() {
        return privilegeEnd;
    }

    public void setPrivilegeEnd(Date privilegeEnd) {
        this.privilegeEnd = privilegeEnd;
    }

    public PrivilegeLevel getPrivilegeLevel() {
        return privilegeLevel;
    }

    public void setPrivilegeLevel(PrivilegeLevel privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
        this.privilegeLevelIndex = this.privilegeLevel.ordinal();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPrivilegeLevelIndex() {
        return privilegeLevelIndex;
    }

    public void setPrivilegeLevelIndex(int privilegeLevelIndex) {
        this.privilegeLevelIndex = privilegeLevelIndex;
    }

    @Override
    public String toString() {
        String pLevel = switch (privilegeLevelIndex) {
            case 0 -> "Myyjä";
            case 1 -> "Myymäläpäällikkö";
            case 2 -> "Järjestelmän ylläpitäjä";
            default -> throw new IllegalStateException("Unexpected value");
        };
        return privilegeStart + ", " + (privilegeEnd == null ? "Ei päättymispäivää" : privilegeEnd) + ", " + pLevel;
    }
}
