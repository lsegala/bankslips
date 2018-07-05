package bookmarks;

import org.springframework.util.StringUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Criado por leonardo.segala em 04/07/2018.
 */
public class BankSlipListener {

    public static final String ERROR_MESSAGE = "Invalid bankslip provided.The possible reasons are:\n" +
            "* A field of the provided bankslip was null or with invalid values";

    @PrePersist
    public void generateUUID(BankSlip obj) throws BankSlipConstraintException {
        obj.setId(UUID.randomUUID().toString());
        validar(obj);
    }

    @PreUpdate
    public void validar(BankSlip obj) throws BankSlipConstraintException {
        if(obj.getDueDate() == null){
            throw new BankSlipConstraintException(ERROR_MESSAGE);
        }
        if(obj.getTotalInCents() == null || obj.getTotalInCents().compareTo(BigDecimal.ZERO) < 0){
            throw new BankSlipConstraintException(ERROR_MESSAGE);
        }
        if(StringUtils.isEmpty(obj.getCustomer())){
            throw new BankSlipConstraintException(ERROR_MESSAGE);
        }
    }
}
