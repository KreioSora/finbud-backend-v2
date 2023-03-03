package com.project.backend.transactions.controller;

import com.project.backend.common.models.AppResponse;
import com.project.backend.transactions.models.requests.TransactionCreateRequest;
import com.project.backend.transactions.models.requests.TransactionUpdateRequest;
import com.project.backend.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/accounts/")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionService transactionService;

    @GetMapping("/{idAccount}/transactions")
    public ResponseEntity<AppResponse> transactionDashboard(@RequestParam(required = false, defaultValue = "0", value = "page") Integer page,
                                                            @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize,
                                                            @RequestParam(required = false, defaultValue = "", value = "nameQuery") String dateQuery,
                                                            @PathVariable Long idAccount) {
        return transactionService.dashboard(page, pageSize, dateQuery.equals("") ? null : dateQuery, idAccount);
    }

    @GetMapping("/{idAccount}/transactions/{idTransact}")
    public ResponseEntity<AppResponse> transactionDetail(@PathVariable Long idAccount,
                                                         @PathVariable String idTransact) {
        return transactionService.transactionDetails(idAccount, idTransact);
    }

    @PostMapping("/{idAccount}/transactions")
    public ResponseEntity<AppResponse> createTransaction(@PathVariable Long idAccount,
                                                         @RequestBody TransactionCreateRequest request) {
        return transactionService.createTransaction(idAccount, request);
    }

    @PutMapping("/{idAccount}/transactions/{idTransact}")
    public ResponseEntity<AppResponse> updateTransaction(@PathVariable Long idAccount,
                                                         @PathVariable String idTransact,
                                                         @RequestBody TransactionUpdateRequest request) {
        return transactionService.updateTransaction(idAccount, idTransact, request);
    }

    @DeleteMapping("/{idAccount}/transactions/{idTransact}")
    public ResponseEntity<AppResponse> deleteTransaction(@PathVariable Long idAccount,
                                                         @PathVariable String idTransact) {
        return transactionService.deleteTransaction(idAccount, idTransact);
    }

}
