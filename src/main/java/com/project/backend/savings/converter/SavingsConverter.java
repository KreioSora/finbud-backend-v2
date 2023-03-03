package com.project.backend.savings.converter;

import com.project.backend.savings.models.Savings;
import com.project.backend.savings.models.SavingsType;
import com.project.backend.savings.models.requests.SavingsCreateRequest;
import com.project.backend.savings.models.requests.SavingsUpdateRequest;
import com.project.backend.savings.repository.SavingsTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class SavingsConverter {

    private final SavingsTypeRepository savingsTypeRepository;

    public Savings createSavingsConverter(SavingsCreateRequest request) {
        return Savings.builder()
                .code(request.getCode())
                .name(request.getName())
                .type(savingsTypeRepository.findByName(request.getType())
                        .orElseThrow(() -> new EntityNotFoundException("Savings Type not found")))
                .updatedAt(OffsetDateTime.now())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public Savings updateSavingsConverter(Savings savings, SavingsUpdateRequest request){
        SavingsType type = request.getType().isBlank() ? savings.getType() : savingsTypeRepository.findByName(request.getType())
                    .orElseThrow(() -> new EntityNotFoundException("Savings Type not found"));
        savings.setName(request.getName().isBlank() ? savings.getName() : request.getName());
        savings.setType(type);
        savings.setCode(request.getCode().isBlank() ? savings.getCode() : request.getCode());
        savings.setUpdatedAt(OffsetDateTime.now());
        return savings;
    }

    public Savings deleteSavingsConverter(Savings savings) {
        savings.setDeleted(true);
        savings.setUpdatedAt(OffsetDateTime.now());
        return savings;
    }

}
