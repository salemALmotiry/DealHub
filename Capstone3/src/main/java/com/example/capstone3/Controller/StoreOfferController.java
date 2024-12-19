package com.example.capstone3.Controller;

import com.example.capstone3.ApiResponse.ApiResponse;
import com.example.capstone3.InDTO.StoreCounterOfferDTOIn;
import com.example.capstone3.OutDTO.StoreCounterOfferDTO;
import com.example.capstone3.Service.StoreOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/store-offer")
// Done by Basil
public class StoreOfferController {

    private final StoreOfferService storeOfferService;
    // get all offer for specific store  - by basil
    @GetMapping("/get-by-store/{storeId}")
    public ResponseEntity<List<StoreCounterOfferDTO>> getStoreOffersByStoreId(@PathVariable Integer storeId) {
        return ResponseEntity.ok(storeOfferService.getStoreOfferByStoreId(storeId));
    }
    // get all by made by individual for specific store - by basil
    @GetMapping("/get-by-store-and-individual/{storeId}/{individualId}")
    public ResponseEntity<List<StoreCounterOfferDTO>> getOffersByIndividualIdAndStoreId(@PathVariable Integer storeId, @PathVariable Integer individualId) {
        return ResponseEntity.ok(storeOfferService.getOfferByIndividualIdAndStoreId(individualId, storeId));
    }
    // Get all offers pending by individual - by basil
    @GetMapping("/pending-by-individual/{individualId}")
    public ResponseEntity<List<StoreCounterOfferDTO>> getStoreOfferPendingByIndividual(@PathVariable Integer individualId) {
        return ResponseEntity.ok( storeOfferService.getStoreOfferPendingByIndividualId(individualId));
    }

    // Get all offers pending by owner - by basil
    @GetMapping("/pending-by-owner/{ownerId}")
    public ResponseEntity<List<StoreCounterOfferDTO>> getStoreOfferPendingByOwner(@PathVariable Integer ownerId) {
        return ResponseEntity.ok(storeOfferService.getStoreOfferPendingByOwnerId(ownerId));
    }

    // Make offer with same deal - by basil
    @PostMapping("/add-same-deal/{storeId}/{individualId}")
    public ResponseEntity<ApiResponse> makeOfferSameDeal(@PathVariable Integer storeId, @PathVariable Integer individualId) {
        storeOfferService.makeOfferSameDeal(individualId, storeId);
        return ResponseEntity.ok(new ApiResponse("Offer with the same deal created successfully"));
    }
    // make offer with counter the original deal - by basil
    @PostMapping("/add-counter-deal/{storeId}/{individualId}")
    public ResponseEntity<ApiResponse> makeOfferWithCounter(@PathVariable Integer storeId, @PathVariable Integer individualId, @RequestBody @Valid StoreCounterOfferDTOIn storeCounterOfferDTOIn) {
        storeOfferService.makeOfferWithCounter(individualId, storeId, storeCounterOfferDTOIn);
        return ResponseEntity.ok(new ApiResponse("Counter offer created successfully"));
    }
    // individual counter back the deal to owner - by basil
    @PutMapping("/counter-by-individual/{offerId}/{individualId}")
    public ResponseEntity<ApiResponse> individualCounterOffer(@PathVariable Integer offerId, @PathVariable Integer individualId, @RequestBody @Valid StoreCounterOfferDTOIn storeCounterOfferDTOIn) {
        storeOfferService.individualCounterOffer(offerId, individualId, storeCounterOfferDTOIn);
        return ResponseEntity.ok(new ApiResponse("Counter offer updated successfully"));
    }

    // individual approve the offer - by basil
    @PutMapping("/approve-by-individual/{offerId}/{individualId}")
    public ResponseEntity<ApiResponse> individualApprove(@PathVariable Integer offerId, @PathVariable Integer individualId) {
        storeOfferService.individualApprove(offerId, individualId);
        return ResponseEntity.ok(new ApiResponse("Offer approved successfully"));
    }

    // individual Reject the offer - by basil
    @PutMapping("/reject-by-individual/{offerId}/{individualId}")
    public ResponseEntity<ApiResponse> individualReject(@PathVariable Integer offerId, @PathVariable Integer individualId) {
        storeOfferService.individualReject(offerId, individualId);
        return ResponseEntity.ok(new ApiResponse("Offer rejected successfully"));
    }
    // Owner counter back the deal to individual - by basil
    @PutMapping("/counter-by-owner/{offerId}/{ownerId}")
    public ResponseEntity<ApiResponse> ownerCounterOffer(@PathVariable Integer offerId, @PathVariable Integer ownerId, @RequestBody @Valid StoreCounterOfferDTOIn storeCounterOfferDTOIn) {
        storeOfferService.ownerCounterOffer(offerId, ownerId, storeCounterOfferDTOIn);
        return ResponseEntity.ok(new ApiResponse("Owner counter offer updated successfully"));
    }
    // Owner approve the offer - by basil
    @PutMapping("/approve-by-owner/{offerId}/{ownerId}")
    public ResponseEntity<ApiResponse> ownerApprove(@PathVariable Integer offerId, @PathVariable Integer ownerId) {
        storeOfferService.ownerApprove(offerId, ownerId);
        return ResponseEntity.ok(new ApiResponse("Offer approved successfully"));
    }
    @PutMapping("/reject-by-owner/{offerId}/{ownerId}")
    public ResponseEntity<ApiResponse> ownerReject(@PathVariable Integer offerId, @PathVariable Integer ownerId) {
        storeOfferService.ownerReject(offerId, ownerId);
        return ResponseEntity.ok(new ApiResponse("Offer rejected successfully"));
    }
}
