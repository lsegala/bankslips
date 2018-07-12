package com.contaazul.bankslips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Criado por leonardo.segala em 12/07/2018.
 */
@Service
@Validated
public class BankslipServiceImpl implements BankslipService {
    private final BankSlipRepository bankSlipRepository;

    @Autowired
    public BankslipServiceImpl(final BankSlipRepository bankSlipRepository) {
        this.bankSlipRepository = bankSlipRepository;
    }

    @Override
    public Optional<BankSlip> findById(String id){
        return bankSlipRepository.findById(id);
    }

    @Override
    @Transactional
    public BankSlip save(BankSlip bankSlip){
        return bankSlipRepository.save(bankSlip);
    }

    @Override
    public List<BankSlip> findAll(){
        return bankSlipRepository.findAll();
    }

    @Override
    @Transactional
    public void cancelPayment(String id){
        BankSlip bankSlip = this.bankSlipRepository.findById(id)
                .orElseThrow(BankSlipNotFoundException::new);
        bankSlip.setStatus(BankSlipStatus.CANCELED);
        this.bankSlipRepository.save(bankSlip);
    }

    @Override
    public void doPayment(String id, Calendar date){
        BankSlip bankSlip = this.bankSlipRepository.findById(id)
                .orElseThrow(BankSlipNotFoundException::new);
        bankSlip.setPaymentDate(date);
        bankSlip.setStatus(BankSlipStatus.PAID);
        this.bankSlipRepository.save(bankSlip);
    }
}
