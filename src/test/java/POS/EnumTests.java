package POS;
import model.classes.*;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumTests extends TestParent{
    @BeforeAll
    public void beforeAll() {
        System.out.println("Enum tests...");
    }


    @Test
    public void CustomerLevel(){
        Assertions.assertEquals(0,CustomerLevel.NONE.ordinal(),"Error in customerlevel enum");
        Assertions.assertEquals(1,CustomerLevel.BONUS.ordinal(), "Error in customerlevel enum");
    }

    @Test
    public void PrivilegeLevel(){
        Assertions.assertEquals(0,PrivilegeLevel.USER.ordinal(),"Error in PrivilegeLevel enum");
    }

    @Test
    public void PaymentMethod(){
        Assertions.assertEquals(0,PaymentMethod.CASH.ordinal(),"Error in Enum Paymentmethod");
    }
}
