package POS;


import model.classes.HWID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HWIDTest extends TestParent{
    @BeforeAll
    public void beforeAll() {
        System.out.println("HWID tests...");
    }

    @Test
    public void osTest(){
        String os = System.getProperty("os.name");
        Assertions.assertEquals(os,HWID.getOperatingSystem(), "Error getting operating system with HWID");
    }
}
