package com.example.capstone3.Controller;

import com.example.capstone3.OutDTO.StoreContractDTO;
import com.example.capstone3.Service.StoreContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/store-contract")

// Done By Basil
public class StoreContractController {

    private final StoreContractService storeContractService;

    // get all store contract by individual - by basil
    @GetMapping("/get-by-individual/{individualId}")
    public ResponseEntity<List<StoreContractDTO>> getAllStoreContractsByIndividual(@PathVariable Integer individualId) {
        return ResponseEntity.ok(storeContractService.getAllStoreContractByIndividual(individualId));
    }
    // get all store contract by owner - by basil
    @GetMapping("/get-by-owner/{ownerId}")
    public ResponseEntity<List<StoreContractDTO>> getAllStoreContractsByOwner(@PathVariable Integer ownerId) {
        return ResponseEntity.ok(storeContractService.getAllStoreContractByOwner(ownerId));
    }
}