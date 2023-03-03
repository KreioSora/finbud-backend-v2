package com.project.backend.savings.models;

import com.project.backend.user.models.User;
import jakarta.persistence.*;
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
@Table(name = "savings_account")
public class SavingsAccount {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Savings savings;

    private String accountName;

    private String accountNumber;

    private Double totalAmount;

    private String currency;

    private boolean isDeleted = false;

    private OffsetDateTime updatedAt;

    private OffsetDateTime createdAt;

}
