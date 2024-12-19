package com.example.capstone3.Controller;

import com.example.capstone3.ApiResponse.ApiResponse;
import com.example.capstone3.InDTO.FranchiseOfferIn;
import com.example.capstone3.Service.FranchiseOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/franchise-offer")

// Done by salem

public class FranchiseOfferController {

    private final FranchiseOfferService franchiseOfferService;


    // individual
    @PostMapping("/individual/make-franchise-offer/{individualId}/{franchiseId}")
    public ResponseEntity<?> makeFranchiseOffer(@PathVariable Integer individualId, @PathVariable Integer franchiseId) {

        franchiseOfferService.individualMakeOfferSameDeal(individualId, franchiseId);
        return ResponseEntity.status(200)
                .body(new ApiResponse("Franchise offer made successfully"));
    }

    @PostMapping("/individual/make-counter-franchise-offer/{individualId}/{franchiseId}")
    public ResponseEntity<?> makeOfferWithCounter(@PathVariable Integer individualId, @PathVariable Integer franchiseId, @RequestBody @Valid FranchiseOfferIn franchiseOfferIn) {

        franchiseOfferService.individualMakeOfferWithCounter(individualId, franchiseId, franchiseOfferIn);
        return ResponseEntity.status(200).body(new ApiResponse("Franchise counter offer made successfully"));
    }

    @GetMapping("/individual/get-offer-pending-py-owner/{individualId}")
    public ResponseEntity<?> getOfferPendingByOwner(@PathVariable Integer individualId){

        return ResponseEntity.status(200).body(franchiseOfferService.getIndividualFranchiseOfferPendingByOwner(individualId));
    }

    @GetMapping("/individual/offer-wait-response/{individualId}")
    public ResponseEntity<?> getOfferWaitResponse(@PathVariable Integer individualId){

        return ResponseEntity.status(200).body(franchiseOfferService.getIndividualFranchiseOfferWaitResponse(individualId));
    }

    @PutMapping("/individual/counter-owner-offer/{individualId}/{offerId}/{franchiseId}")
    public ResponseEntity<?> individualCounterOwnerOffer(@PathVariable Integer individualId, @PathVariable Integer offerId, @PathVariable Integer franchiseId, @RequestBody @Valid FranchiseOfferIn franchiseOfferIn) {

        franchiseOfferService.individualCounterOwnerOffer(individualId, offerId, franchiseId, franchiseOfferIn);
        return ResponseEntity.status(200).body(new ApiResponse("Counter offer by owner processed successfully"));
    }

    @PutMapping("/individual/approve-offer/{franchiseOfferId}/{individualId}")
    public ResponseEntity<?> individualApprovedOffer(@PathVariable Integer franchiseOfferId, @PathVariable Integer individualId) {

        franchiseOfferService.individualApprovedOffer(franchiseOfferId, individualId);
        return ResponseEntity.status(200).body(new ApiResponse("Franchise offer approved successfully"));
    }

    @PutMapping("/individual/reject-offer/{franchiseOfferId}/{individualId}")
    public ResponseEntity<?> individualRejectedOffer(@PathVariable Integer franchiseOfferId, @PathVariable Integer individualId) {
        franchiseOfferService.individualRejectedOffer(franchiseOfferId, individualId);
        return ResponseEntity.status(200).body(new ApiResponse("Franchise offer rejected successfully"));
    }

    // Owner

    @GetMapping("/owner/offer-wait-response/{ownerId}/{franchiseId}")
    public ResponseEntity<?> getOfferWaitResponseOwner(@PathVariable Integer ownerId,@PathVariable Integer franchiseId ) {
        return ResponseEntity.status(200).body(franchiseOfferService.getOwnerFranchiseOfferWaitResponse(ownerId,franchiseId));

    }

    @PutMapping("/owner/accept-offer/{ownerId}/{franchiseId}")
    public ResponseEntity<?> acceptOfferOwner(@PathVariable Integer ownerId, @PathVariable Integer franchiseId) {

        franchiseOfferService.ownerAcceptFranchiseOffer(ownerId, franchiseId);

        return ResponseEntity.status(200).body(new ApiResponse("Franchise offer accepted successfully"));
    }

    @PutMapping("/owner/make-counter-franchise-offer/{ownerId}/{offerId}/{franchiseId}")
    public ResponseEntity<?> makeOfferWithCounter(@PathVariable Integer ownerId, @PathVariable Integer offerId, @PathVariable Integer franchiseId, @RequestBody @Valid FranchiseOfferIn franchiseOfferIn) {
        franchiseOfferService.ownerMakeFranchiseCounterForIndividual(ownerId, offerId, franchiseId, franchiseOfferIn);
        return ResponseEntity.status(200).body(new ApiResponse("Franchise counter offer made successfully"));
    }


    @PutMapping("/owner/reject-offer/{franchiseOfferId}/{ownerId}")
    public ResponseEntity<?> ownerRejectFranchiseOffer(@PathVariable Integer franchiseOfferId, @PathVariable Integer ownerId) {

        franchiseOfferService.ownerRejectFranchiseOffer(franchiseOfferId, ownerId);
        return ResponseEntity.status(200)
                .body(new ApiResponse("Franchise offer rejected by owner successfully"));
    }

    @GetMapping("/owner/get-offer-pending-py-individual/{ownerId}")
    public ResponseEntity<?> getOfferPendingByIndividual(@PathVariable Integer ownerId){

        return ResponseEntity.status(200).body(franchiseOfferService.getOwnerFranchiseOfferPendingByIndividual(ownerId));
    }





}
