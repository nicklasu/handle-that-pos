package POS;

import model.classes.Privilege;
import model.classes.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Date;

import static model.classes.PrivilegeLevel.USER;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrivilegeTest {
    private Privilege privilege;
    private long time;

    @BeforeAll
    public void beforeAll() {
        System.out.println("Starting privilegeTests...");
        privilege = new Privilege();
    }

    @Test
    public void privilegeSetGetTest() {
        time = System.currentTimeMillis();
        final long timeToAdd = Long.MAX_VALUE - time;
        final Date date = new Date(time);
        final Date dateEnd = new Date(time + timeToAdd);
        final User user = new User();
        privilege.setUser(user);
        privilege.setPrivilegeLevel(USER);
        privilege.setId(1);
        privilege.setPrivilegeStart(date);
        privilege.setPrivilegeEnd(dateEnd);
        privilege.setPrivilegeLevelIndex(0);
        Assertions.assertEquals(privilege.getUser(), user, "User incorrect!");
        Assertions.assertEquals(privilege.getPrivilegeLevel(), USER, "Privilegelevel incorrect!");
        Assertions.assertEquals(1, privilege.getId(), "PrivilegeId incorrect!");
        Assertions.assertEquals(privilege.getPrivilegeStart(), date, "Privilege start date incorrect!");
        Assertions.assertEquals(privilege.getPrivilegeEnd(), dateEnd, "Privilege end date incorrect!");
        Assertions.assertEquals(0, privilege.getPrivilegeLevelIndex(), "PrivilegeLevelIndex is incorrect!");
    }

    @Test
    public void privilegeToStringTest() {
        Assertions.assertEquals(privilege.toString(), new Date(time) + ", 8994-08-17, Itsepalvelukassa",
                "Privilege toString doesn't work correctly!");
    }
}
