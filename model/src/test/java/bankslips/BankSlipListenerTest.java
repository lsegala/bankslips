package bankslips;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.Assert.assertNotNull;

/**
 * Criado por leonardo.segala em 05/07/2018.
 */
public class BankSlipListenerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public static final String CUSTOMER = "customer";
    public static final Calendar DUE_DATE = Calendar.getInstance();
    public static final BigDecimal TOTAL_IN_CENTS = BigDecimal.valueOf(10000);
    private BankSlipListener listener;

    @Before
    public void init(){
        listener = new BankSlipListener();
    }

    @Test
    public void mustGenerateUUIDKey() throws BankSlipConstraintException {
        BankSlip obj = new BankSlip(CUSTOMER, DUE_DATE, TOTAL_IN_CENTS);
        listener.generateUUID(obj);
        assertNotNull(obj.getId());
    }

    @Test
    public void mustValidateNullDueDate() throws BankSlipConstraintException {
        BankSlip obj = new BankSlip(CUSTOMER, null, TOTAL_IN_CENTS);
        thrown.expect(BankSlipConstraintException.class);
        listener.validar(obj);
    }

    @Test
    public void mustValidateNullTotalInCents() throws BankSlipConstraintException {
        BankSlip obj = new BankSlip(CUSTOMER, DUE_DATE, null);
        thrown.expect(BankSlipConstraintException.class);
        listener.validar(obj);
    }

    @Test
    public void mustValidateNegativeTotalInCents() throws BankSlipConstraintException {
        BankSlip obj = new BankSlip(CUSTOMER, DUE_DATE, BigDecimal.valueOf(-1));
        thrown.expect(BankSlipConstraintException.class);
        listener.validar(obj);
    }

    @Test
    public void mustValidateCustomerNull() throws BankSlipConstraintException {
        BankSlip obj = new BankSlip(null, DUE_DATE, TOTAL_IN_CENTS);
        thrown.expect(BankSlipConstraintException.class);
        listener.validar(obj);
    }


    @Test
    public void mustValidateCustomerEmpty() throws BankSlipConstraintException {
        BankSlip obj = new BankSlip("", DUE_DATE, TOTAL_IN_CENTS);
        thrown.expect(BankSlipConstraintException.class);
        listener.validar(obj);
    }
}
