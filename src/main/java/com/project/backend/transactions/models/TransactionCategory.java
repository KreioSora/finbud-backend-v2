package com.project.backend.transactions.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "transactions_category")
public class TransactionCategory {
    @Id
    private String id;

    private String name;

    private boolean isDeleted = false;

    private OffsetDateTime updatedAt;

    private OffsetDateTime createdAt;
}
