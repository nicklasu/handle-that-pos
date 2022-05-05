package POS;


import model.classes.HWID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * HWIDTest
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HWIDTest extends TestParent{
    /**
     * Before all tests, do a system printout.
     */
    @BeforeAll
    public void beforeAll() {
        System.out.println("HWID tests...");
    }

    /**
     * Test of which operating system is being used.
     */
    @Test
    public void osTest(){
        String os = System.getProperty("os.name");
        Assertions.assertEquals(os,HWID.getOperatingSystem(), "Error getting operating system with HWID");
    }
}
