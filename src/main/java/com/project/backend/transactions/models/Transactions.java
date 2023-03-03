package com.project.backend.transactions.models;

import com.project.backend.savings.models.SavingsAccount;
import com.project.backend.savings.models.SavingsPockets;
import com.project.backend.user.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transactions {

    @Id
    private String id;

    @ManyToOne
    private User user;

    @ManyToOne
    private SavingsAccount account;

    @ManyToOne
    private SavingsPockets pockets;

    private String currency;

    private Double amount;

    private TransactionType type;

    private boolean isDeleted = false;

    private OffsetDateTime createdAt;

}
