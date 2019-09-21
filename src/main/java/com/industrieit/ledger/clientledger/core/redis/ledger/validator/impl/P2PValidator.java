package com.industrieit.ledger.clientledger.core.redis.ledger.validator.impl;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.ledger.validator.Validator;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Itemizable;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.impl.P2PItemizable;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.P2PRequest;
import com.industrieit.ledger.clientledger.core.redis.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class P2PValidator implements Validator<P2PRequest> {
    private final AccountRepository accountRepository;

    public P2PValidator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Itemizable validate(String requestId, P2PRequest request) {

        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBusinessRuleException("Negative amount not supported");
        }
        if (request.getFee().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBusinessRuleException("Negative fee not supported");
        }
        if (request.getTax().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBusinessRuleException("Negative tax not supported");
        }

        Optional<Account> sourceAccountOptional = accountRepository.findById(request.getFromCustomerAccount());
        if (!sourceAccountOptional.isPresent()) {
            throw new InvalidBusinessRuleException("Source account not found");
        } else {
            if (!sourceAccountOptional.get().getCurrency().equals(request.getCurrency())) {
                throw new InvalidBusinessRuleException("Currency exchange not supported for source account");
            }
        }

        Optional<Account> destinationAccountOptional = accountRepository.findById(request.getToCustomerAccount());
        if (!destinationAccountOptional.isPresent()) {
            throw new InvalidBusinessRuleException("Destination account not found");
        } else {
            if (!destinationAccountOptional.get().getCurrency().equals(request.getCurrency())) {
                throw new InvalidBusinessRuleException("Currency exchange not supported for destination account");
            }
        }
        Optional<Account> feeAccountOptional = accountRepository.findById(request.getFeeAccount());
        if (!feeAccountOptional.isPresent()) {
            throw new InvalidBusinessRuleException("Fee account not found");
        } else {
            if (!feeAccountOptional.get().getCurrency().equals(request.getCurrency())) {
                throw new InvalidBusinessRuleException("Currency exchange not supported for fee account");
            }
        }
        Optional<Account> taxAccountOptional = accountRepository.findById(request.getTaxAccount());
        if (!taxAccountOptional.isPresent()) {
            throw new InvalidBusinessRuleException("Tax account not found");
        } else {
            if (!taxAccountOptional.get().getCurrency().equals(request.getCurrency())) {
                throw new InvalidBusinessRuleException("Currency exchange not supported for tax account");
            }
        }
        BigDecimal sourceFinalBalance = sourceAccountOptional.get().getBalance();
        BigDecimal totalFundNeeded = request.getAmount().add(request.getFee()).add(request.getTax());
        if (sourceFinalBalance.compareTo(totalFundNeeded) < 0) {
            throw new InvalidBusinessRuleException("Not enough fund to P2P");
        }
        return new P2PItemizable(sourceAccountOptional.get(), destinationAccountOptional.get(), feeAccountOptional.get(),
                taxAccountOptional.get(), request.getAmount(), request.getFee(), request.getTax(), request.getCurrency(), requestId);
    }


}
