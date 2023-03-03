package com.project.backend.transactions.converter;

import com.project.backend.savings.models.SavingsAccount;
import com.project.backend.savings.models.SavingsPockets;
import com.project.backend.savings.models.requests.PocketCreateRequest;
import com.project.backend.savings.models.requests.PocketUpdateRequest;
import com.project.backend.savings.repository.SavingsAccountRepository;
import com.project.backend.savings.repository.SavingsPocketsRepository;
import com.project.backend.savings.repository.SavingsRepository;
import com.project.backend.transactions.models.TransactionType;
import com.project.backend.transactions.models.Transactions;
import com.project.backend.transactions.models.requests.TransactionCreateRequest;
import com.project.backend.user.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class TransactionConverter {

    public Transactions createTransactionConverter(TransactionCreateRequest request, User user, SavingsAccount account, SavingsPockets pockets) {
        OffsetDateTime createdAt = OffsetDateTime.now();
        String id = String.format("%s@%s", createdAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), account.getId());
        return Transactions.builder()
                .id(id)
                .user(user)
                .account(account)
                .pockets(pockets)
                .currency(request.getCurrency())
                .amount(request.getAmount())
                .type(TransactionType.getAccountStatusByValue(request.getType())
                        .orElseThrow(() -> new RuntimeException("Type not found")))
                .isDeleted(false)
                .createdAt(createdAt)
                .build();
    }

    public SavingsPockets updatePocketConverter(SavingsPockets pockets, PocketUpdateRequest request) {
        pockets.setTitle(request.getTitle().isBlank() ? pockets.getTitle() : request.getTitle());
        pockets.setAmount(request.getAmount() == null ? pockets.getAmount() : request.getAmount());
        pockets.setGoalAmount(request.getGoalAmount() == null ? pockets.getGoalAmount(): request.getGoalAmount());
        pockets.setUpdatedAt(OffsetDateTime.now());
        return pockets;
    }

    public SavingsPockets updateAmountConverter(SavingsPockets pockets, Double change) {
        pockets.setAmount(pockets.getAmount() + change);
        pockets.setUpdatedAt(OffsetDateTime.now());
        return pockets;
    }

    public SavingsPockets deletePocketConverter(SavingsPockets pockets) {
        pockets.setDeleted(true);
        pockets.setUpdatedAt(OffsetDateTime.now());
        return pockets;
    }
}
