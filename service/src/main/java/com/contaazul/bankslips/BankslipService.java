package com.contaazul.bankslips;

import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Criado por leonardo.segala em 12/07/2018.
 */
public interface BankslipService {
    Optional<BankSlip> findById(String id);

    @Transactional
    BankSlip save(BankSlip bankSlip);

    List<BankSlip> findAll();

    @Transactional
    void cancelPayment(String id);

    void doPayment(String id, Calendar date);
}
