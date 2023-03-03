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
@Table(name = "savings_pockets")
public class SavingsPockets {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private SavingsAccount account;

    private String title;

    private Double amount;

    private Double goalAmount;

    private String currency;

    private boolean isDeleted = false;

    private OffsetDateTime updatedAt;

    private OffsetDateTime createdAt;

}
