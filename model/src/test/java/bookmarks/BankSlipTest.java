package bookmarks;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Criado por leonardo.segala em 04/07/2018.
 */
public class BankSlipTest {

    public static final String COMPANY = "Ford Prefect Company";

    @Test
    public void testTotalObjectNullable(){
        BankSlip bankSlip = new BankSlip(null, null, null);
        assertNull(bankSlip.getTotalInCents());
    }

    @Test
    public void testTotalObjectWithoutTaxes(){
        BigDecimal totalInCents = new BigDecimal(10000);
        BankSlip bankSlip = new BankSlip(COMPANY, Calendar.getInstance(), totalInCents);
        assertEquals(totalInCents.longValue(), bankSlip.getTotalInCents().longValue());
    }

    @Test
    public void testTotalObjectWithLess10daysLate(){
        BigDecimal totalInCents = new BigDecimal(10000);
        BigDecimal expectedTotal = totalInCents.add(totalInCents.multiply(BigDecimal.valueOf(0.005D)).multiply(BigDecimal.valueOf(2)));
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DATE, -2);
        BankSlip bankSlip = new BankSlip(COMPANY, dueDate, totalInCents);
        assertEquals(expectedTotal.longValue(), bankSlip.getTotalInCents().longValue());
    }

    @Test
    public void testTotalObjectWithMore10daysLate(){
        BigDecimal totalInCents = new BigDecimal(10000);
        BigDecimal expectedTotal = totalInCents.add(totalInCents.multiply(BigDecimal.valueOf(0.01D)).multiply(BigDecimal.valueOf(11)));
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DATE, -11);
        BankSlip bankSlip = new BankSlip(COMPANY, dueDate, totalInCents);
        assertEquals(expectedTotal.longValue(), bankSlip.getTotalInCents().longValue());
    }
}
