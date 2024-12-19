package com.example.capstone3.Controller;

import com.example.capstone3.ApiResponse.ApiResponse;
import com.example.capstone3.InDTO.RentalContractDTOIn;
import com.example.capstone3.Model.RentalContract;
import com.example.capstone3.OutDTO.DetailRentalOfferDTO;
import com.example.capstone3.OutDTO.RentalContractDTO;
import com.example.capstone3.Service.RentalContractService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/rental-contract")
public class RentalContractController {
    // by alaa

    private final RentalContractService rentalContractService;

    //update auto base on contract end date
    @Scheduled(cron = "0 0 0 * * *")
    public void updateExpiredContractsDaily() {
        rentalContractService.updateExpiredContract();
    }


    //endpoint
    @PostMapping("/create-contract")
    public ResponseEntity<?> createRentalContract(@RequestBody @Valid  RentalContractDTOIn rentalContract) {
        rentalContractService.createRentalContract(rentalContract);
        return ResponseEntity.status(200).body(new ApiResponse("Contract created successfully"));
    }

    //endpoint
    @GetMapping("/rental-offer/{rentalOfferId}")
    public ResponseEntity<RentalContractDTO> getRentalContractByRentalOfferId(@PathVariable Integer rentalOfferId) {
        RentalContractDTO rentalContractDTO = rentalContractService.getRentalContractByRentalOfferId(rentalOfferId);
        return ResponseEntity.status(200).body(rentalContractDTO);
    }

    //endpoint
    @GetMapping("/individual/{individualId}/status/{status}")
    public ResponseEntity<?> getAllRentalContractOfIndividualAndStatus(@PathVariable Integer individualId, @PathVariable String status) {
        List<RentalContractDTO> rentalContracts = rentalContractService.getAllRentalContractOfIndividualAndStatus(individualId, status);
        return ResponseEntity.status(200).body(rentalContracts);
    }

    //endpoint
    @GetMapping("/owner/{ownerId}/status/{status}")
    public ResponseEntity<?> getAllRentalContractOfOwnerAndStatus(@PathVariable Integer ownerId, @PathVariable String status) {
        List<RentalContractDTO> rentalContracts = rentalContractService.getAllRentalContractOfOwnerAndStatus(ownerId, status);
        return ResponseEntity.status(200).body(rentalContracts);
    }



}



