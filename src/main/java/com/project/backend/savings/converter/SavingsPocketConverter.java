package com.project.backend.savings.converter;

import com.project.backend.savings.models.SavingsAccount;
import com.project.backend.savings.models.SavingsPockets;
import com.project.backend.savings.models.requests.AccountCreateRequest;
import com.project.backend.savings.models.requests.AccountUpdateRequest;
import com.project.backend.savings.models.requests.PocketCreateRequest;
import com.project.backend.savings.models.requests.PocketUpdateRequest;
import com.project.backend.savings.repository.SavingsAccountRepository;
import com.project.backend.savings.repository.SavingsPocketsRepository;
import com.project.backend.savings.repository.SavingsRepository;
import com.project.backend.user.models.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class SavingsPocketConverter {

    private final SavingsRepository savingsRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final SavingsPocketsRepository savingsPocketsRepository;

    public SavingsPockets createPocketConverter(PocketCreateRequest request, User user, SavingsAccount account) {
        return SavingsPockets.builder()
                .user(user)
                .account(account)
                .title(request.getTitle())
                .amount(request.getAmount() == null ? request.getAmount() : 0)
                .goalAmount(request.getGoalAmount())
                .currency(request.getCurrency())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
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
