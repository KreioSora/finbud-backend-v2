package com.project.backend.savings.converter;

import com.project.backend.savings.models.Savings;
import com.project.backend.savings.models.SavingsAccount;
import com.project.backend.savings.models.SavingsType;
import com.project.backend.savings.models.requests.AccountCreateRequest;
import com.project.backend.savings.models.requests.AccountUpdateRequest;
import com.project.backend.savings.models.requests.SavingsCreateRequest;
import com.project.backend.savings.models.requests.SavingsUpdateRequest;
import com.project.backend.savings.repository.SavingsRepository;
import com.project.backend.savings.repository.SavingsTypeRepository;
import com.project.backend.user.models.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class SavingsAccountConverter {

    private final SavingsRepository savingsRepository;

    public SavingsAccount createAccountConverter(AccountCreateRequest request, User user) {
        return SavingsAccount.builder()
                .savings(savingsRepository.findById(request.getSavingsId())
                        .orElseThrow(() -> new EntityNotFoundException("Savings not found")))
                .accountName(request.getAccountName())
                .accountNumber(request.getAccountNumber())
                .user(user)
                .totalAmount(0D)
                .currency(request.getCurrency())
                .updatedAt(OffsetDateTime.now())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public SavingsAccount updateAccountConverter(SavingsAccount account, AccountUpdateRequest request) {
        account.setAccountName(request.getAccountName());
        account.setAccountNumber(request.getAccountNumber().isBlank() ? account.getAccountNumber() : request.getAccountNumber());
        account.setCurrency(request.getCurrency().isBlank() ? account.getCurrency() : request.getCurrency());
        account.setUpdatedAt(OffsetDateTime.now());
        return account;
    }

    public SavingsAccount updateAmountConverter(SavingsAccount account, Double change) {
        account.setTotalAmount(account.getTotalAmount() + change);
        account.setUpdatedAt(OffsetDateTime.now());
        return account;
    }

    public SavingsAccount deleteAccountConverter(SavingsAccount account) {
        account.setDeleted(true);
        account.setUpdatedAt(OffsetDateTime.now());
        return account;
    }
}
