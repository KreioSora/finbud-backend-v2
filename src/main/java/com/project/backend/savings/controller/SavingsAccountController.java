package com.project.backend.savings.controller;

import com.project.backend.common.models.AppResponse;
import com.project.backend.savings.models.requests.AccountCreateRequest;
import com.project.backend.savings.models.requests.AccountUpdateRequest;
import com.project.backend.savings.service.SavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    @GetMapping("/")
    public ResponseEntity<AppResponse> accountsDashboard(@RequestParam(required = false, defaultValue = "0", value = "page") Integer page,
                                                         @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize,
                                                         @RequestParam(required = false, defaultValue = "", value = "nameQuery") String nameQuery) {
        return savingsAccountService.dashboard(page, pageSize, nameQuery.equals("") ? null : nameQuery);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> detailedAccount(@PathVariable Long id) {
        return savingsAccountService.accountDetail(id);
    }

    @PostMapping("/")
    public ResponseEntity<AppResponse> addAccount(@RequestBody AccountCreateRequest request) {
        return savingsAccountService.createAccount(request);
    }

    @PatchMapping("/{id}/amount")
    public ResponseEntity<AppResponse> updateAccountAmount(@PathVariable Long id,
                                                           @RequestBody Double change){
        return savingsAccountService.updateAccountAmount(id, change);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateAccount(@PathVariable Long id,
                                                     @RequestBody AccountUpdateRequest request) {
        return savingsAccountService.updateAccount(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse> deleteAccount(@PathVariable Long id) {
        return savingsAccountService.deleteAccount(id);
    }

}
