package com.contaazul.bankslips;

import javax.persistence.PrePersist;
import java.util.Optional;
import java.util.UUID;

/**
 * Criado por leonardo.segala em 04/07/2018.
 */
public class BankSlipListener {
    @PrePersist
    public void generateUUID(BankSlip obj) throws BankSlipConstraintException {
        obj.setId(UUID.randomUUID().toString());
        obj.setStatus(Optional.ofNullable(obj.getStatus()).orElse(BankSlipStatus.PENDING));
    }
}
