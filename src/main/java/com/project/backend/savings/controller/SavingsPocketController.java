package com.project.backend.savings.controller;

import com.project.backend.common.models.AppResponse;
import com.project.backend.savings.models.requests.PocketCreateRequest;
import com.project.backend.savings.models.requests.PocketUpdateRequest;
import com.project.backend.savings.service.SavingsPocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class SavingsPocketController {

    private final SavingsPocketService savingsPocketService;

    @GetMapping("/{idAccount}/pockets")
    public ResponseEntity<AppResponse> accountPocket(@RequestParam(required = false, defaultValue = "0", value = "page") Integer page,
                                                     @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize,
                                                     @PathVariable Long idAccount) {
        return savingsPocketService.accountPocket(page, pageSize, idAccount);
    }

    @GetMapping("/{idAccount}/pockets/{idPocket}")
    public ResponseEntity<AppResponse> detailedPocket(@PathVariable Long idAccount,
                                                      @PathVariable Long idPocket) {
        return savingsPocketService.pocket(idAccount, idPocket);
    }

    @PostMapping("/{idAccount}/pockets")
    public ResponseEntity<AppResponse> addPocket(@PathVariable Long idAccount,
                                                 @RequestBody PocketCreateRequest request) {
        return savingsPocketService.addPocket(idAccount, request);
    }

    @PutMapping("/{idAccount}/pockets/{idPocket}")
    public ResponseEntity<AppResponse> updatePocket(@PathVariable Long idAccount,
                                                    @PathVariable Long idPocket,
                                                    @RequestBody PocketUpdateRequest request) {
        return savingsPocketService.updatePocket(idAccount, idPocket, request);
    }

    @PatchMapping("/{idAccount}/pockets/{idPocket}/amount")
    public ResponseEntity<AppResponse> updatePocketAmount(@PathVariable Long idAccount,
                                                          @PathVariable Long idPocket,
                                                          @RequestBody Double change) {
        return savingsPocketService.updatePocketAmount(idAccount, idPocket, change);
    }

    @DeleteMapping("{idAccount}/pockets/{idPocket}")
    public ResponseEntity<AppResponse> deletePocket(@PathVariable Long idAccount,
                                                    @PathVariable Long idPocket) {
        return savingsPocketService.deletePocket(idAccount, idPocket);
    }

}
