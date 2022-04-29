package model.classes;

import javax.persistence.*;
import java.sql.Date;

/**
 * Represents a use privilege given to a user account
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Entity
@Table(name = "Käyttöoikeus")
public class Privilege {

    /**
     * Unique identifier of the privilege
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private int id;

    /**
     * Start time of the privilege
     */
    @Column(name = "Oikeusalku")
    private Date privilegeStart;

    /**
     * End time of the privilege
     */
    @Column(name = "Oikeusloppu")
    private Date privilegeEnd;

    /**
     * Tier of access for the privilege as an int
     */
    @Column(name = "KäyttäjätasoID")
    private int privilegeLevelIndex;

    /**
     * Tier of access for the privilege as a "PrivilegeLevel" Enum
     */
    @Transient
    private PrivilegeLevel privilegeLevel;

    /**
     * The user object associated with the privilege
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "KäyttäjäID", referencedColumnName = "id")
    private User user;

    /**
     * Empty constructor required for hibernate
     */
    public Privilege() {

    }

    /**
     * Constructor for creating a privilege
     * 
     * @param user           associated user account
     * @param privilegeStart starting date of privilege
     * @param privilegeEnd   ending date of privilege
     * @param privilegeLevel access level of privilege
     */
    public Privilege(final User user, final Date privilegeStart, final Date privilegeEnd,
            final PrivilegeLevel privilegeLevel) {
        this.user = user;
        this.privilegeLevel = privilegeLevel; // Oletuksena myyjän oikeudet
        this.privilegeLevelIndex = privilegeLevel.ordinal();
        this.privilegeStart = privilegeStart;
        this.privilegeEnd = privilegeEnd;
    }

    /**
     * Constructor for creating a privilege with no connection to a user used when
     * showing user privileges to user
     * 
     * @param privilegeStart starting date of privilege
     * @param privilegeEnd   ending date of privilege
     * @param privilegeLevel access level of privilege
     */
    public Privilege(final Date privilegeStart, final Date privilegeEnd, final PrivilegeLevel privilegeLevel) {
        this.privilegeLevel = privilegeLevel; // Oletuksena myyjän oikeudet
        this.privilegeLevelIndex = privilegeLevel.ordinal();
        this.privilegeStart = privilegeStart;
        this.privilegeEnd = privilegeEnd;
    }

    /**
     * @return identifier for a privilege
     */
    public int getId() {
        return id;
    }

    /**
     * @param id identifier for a privilege
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return starting date for a privilege
     */
    public Date getPrivilegeStart() {
        return privilegeStart;
    }

    /**
     * @param privilegeStart starting date for a privilege
     */
    public void setPrivilegeStart(final Date privilegeStart) {
        this.privilegeStart = privilegeStart;
    }

    /**
     * @return ending date for a privilege
     */
    public Date getPrivilegeEnd() {
        return privilegeEnd;
    }

    /**
     * @param privilegeEnd ending date for a privilege
     */
    public void setPrivilegeEnd(final Date privilegeEnd) {
        this.privilegeEnd = privilegeEnd;
    }

    /**
     * @return Enum PrivilegeLevel denoting level of access
     */
    public PrivilegeLevel getPrivilegeLevel() {
        return privilegeLevel;
    }

    /**
     * @param privilegeLevel Enum denoting level of access
     */
    public void setPrivilegeLevel(final PrivilegeLevel privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
        this.privilegeLevelIndex = this.privilegeLevel.ordinal();
    }

    /**
     * @return user associated with the privilege
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user associated with the privilege
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * @return Ordinal value of Enum PrivilegeLevel
     */
    public int getPrivilegeLevelIndex() {
        return privilegeLevelIndex;
    }

    /**
     * @param privilegeLevelIndex Ordinal value of Enum PrivilegeLevel
     */
    public void setPrivilegeLevelIndex(final int privilegeLevelIndex) {
        this.privilegeLevelIndex = privilegeLevelIndex;
    }

    /**
     * @return contents of Privilege object in string format
     */
    @Override
    public String toString() {
        final String pLevel = switch (privilegeLevelIndex) {
            case 0 -> "Itsepalvelukassa";
            case 1 -> "Myyjä";
            case 2 -> "Myymäläpäällikkö";
            case 3 -> "Järjestelmän ylläpitäjä";
            default -> throw new IllegalStateException("Unexpected value");
        };
        return privilegeStart + ", " + (privilegeEnd == null ? "Ei päättymispäivää" : privilegeEnd) + ", " + pLevel;
    }
}
