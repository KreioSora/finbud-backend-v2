package com.project.backend.transactions.converter;

import com.project.backend.savings.models.SavingsAccount;
import com.project.backend.savings.models.SavingsPockets;
import com.project.backend.transactions.models.TransactionType;
import com.project.backend.transactions.models.Transactions;
import com.project.backend.transactions.models.requests.TransactionCreateRequest;
import com.project.backend.transactions.models.requests.TransactionUpdateRequest;
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
                .updatedAt(createdAt)
                .createdAt(createdAt)
                .build();
    }

    public Transactions updateTransactionConverter(TransactionUpdateRequest request, Transactions transaction, SavingsPockets pocket) {
        transaction.setPockets(pocket);
        transaction.setCurrency(request.getCurrency());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.getAccountStatusByValue(request.getType())
                .orElseThrow(() -> new RuntimeException("Type not found")));
        transaction.setUpdatedAt(OffsetDateTime.now());
        return transaction;
    }

    public Transactions deleteTransactionConverter(Transactions transaction) {
        transaction.setDeleted(true);
        transaction.setUpdatedAt(OffsetDateTime.now());
        return transaction;
    }
}
