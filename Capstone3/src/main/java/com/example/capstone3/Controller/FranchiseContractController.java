package com.example.capstone3.Controller;

import com.example.capstone3.Model.FranchiseContracts;
import com.example.capstone3.Service.FranchiseContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/franchise-contract")

// Done by Salem
public class FranchiseContractController {


    private final FranchiseContractService franchiseContractService;

    @GetMapping("/individual/contracts/active/{individualId}")
    public ResponseEntity getActiveContractsForIndividual(@PathVariable Integer individualId) {
        return ResponseEntity.status(200).body(franchiseContractService.getActiveContracts(individualId));
    }

    @GetMapping("/individualId/contracts/expired/{individualId}")
    public ResponseEntity getExpiredContractsForIndividual(@PathVariable Integer individualId) {
        return ResponseEntity.status(200).body(franchiseContractService.getExpiredContracts(individualId));
    }


    @GetMapping("/owner/contracts/active/{ownerId}")
    public ResponseEntity getActiveContracts(@PathVariable Integer ownerId) {
        return ResponseEntity.status(200).body(franchiseContractService.getActiveContractsOwner(ownerId));
    }

    @GetMapping("/owner/contracts/expired/{ownerId}")
    public ResponseEntity getExpiredContracts(@PathVariable Integer ownerId) {
        return ResponseEntity.status(200).body(franchiseContractService.getExpiredContractsOwner(ownerId));
    }


}
