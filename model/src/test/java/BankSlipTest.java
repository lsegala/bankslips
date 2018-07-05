import bookmarks.BankSlip;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Criado por leonardo.segala em 04/07/2018.
 */
public class BankSlipTest {
    @Test
    public void testTotalObjectNullable(){
        BankSlip bankSlip = new BankSlip(null, null, null);
        assertNull(bankSlip.getTotalInCents());
    }

    @Test
    public void testTotalObjectWithoutTaxes(){
        BigDecimal totalInCents = new BigDecimal(10000);
        BankSlip bankSlip = new BankSlip("Ford Prefect Company", Calendar.getInstance(), totalInCents);
        assertEquals(totalInCents, bankSlip.getTotalInCents());
    }

    @Test
    public void testTotalObjectWithLess10daysLate(){
        BigDecimal totalInCents = new BigDecimal(10000);
        BigDecimal expectedTotal = totalInCents.add(totalInCents.multiply(new BigDecimal(0.005D)).multiply(new BigDecimal(2)));
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DATE, -2);
        BankSlip bankSlip = new BankSlip("Ford Prefect Company", dueDate, totalInCents);
        assertEquals(expectedTotal, bankSlip.getTotalInCents());
    }

    @Test
    public void testTotalObjectWithMore10daysLate(){
        BigDecimal totalInCents = new BigDecimal(10000);
        BigDecimal expectedTotal = totalInCents.add(totalInCents.multiply(new BigDecimal(0.01D)).multiply(new BigDecimal(11)));
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DATE, -11);
        BankSlip bankSlip = new BankSlip("Ford Prefect Company", dueDate, totalInCents);
        assertEquals(expectedTotal, bankSlip.getTotalInCents());
    }
}
