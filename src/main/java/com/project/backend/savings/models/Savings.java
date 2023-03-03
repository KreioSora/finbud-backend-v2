package com.project.backend.savings.models;

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
@Table(name = "savings")
public class Savings {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String code;

    @ManyToOne
    private SavingsType type;

    private boolean isDeleted = false;
    
    private OffsetDateTime updatedAt;
    
    private OffsetDateTime createdAt;

}
