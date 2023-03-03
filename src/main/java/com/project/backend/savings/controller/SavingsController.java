package com.project.backend.savings.controller;

import com.project.backend.common.models.AppResponse;
import com.project.backend.savings.service.SavingsService;
import com.project.backend.savings.models.requests.SavingsCreateRequest;
import com.project.backend.savings.models.requests.SavingsUpdateRequest;
import com.project.backend.savings.validation.savings.CreateSavingsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;
    private final CreateSavingsValidator createSavingsValidator;

    @GetMapping("/")
    public ResponseEntity<AppResponse> savingsDashboard(@RequestParam(required = false, defaultValue = "0", value = "page") Integer page,
                                                        @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize,
                                                        @RequestParam(required = false, defaultValue = "", value = "nameQuery") String nameQuery) {
        return savingsService.dashboard(page, pageSize, nameQuery.equals("") ? null : nameQuery);
    }

    @GetMapping("/{code}")
    public ResponseEntity<AppResponse> detailedSavings(@PathVariable String code) {
        return savingsService.savingsDetail(code);
    }

    @PostMapping("/")
    public ResponseEntity<AppResponse> addSavings(@RequestBody SavingsCreateRequest request) {
        return savingsService.addSavings(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateSavings(@PathVariable Long id,
                                                     @RequestBody SavingsUpdateRequest request) {
        return savingsService.updateSavings(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse> deleteSavings(@PathVariable Long id) {
        return savingsService.deleteSavings(id);
    }

}
