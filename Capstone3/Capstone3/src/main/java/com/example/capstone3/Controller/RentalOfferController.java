package com.example.capstone3.Controller;

import com.example.capstone3.ApiResponse.ApiResponse;
import com.example.capstone3.OutDTO.DetailRentalOfferDTO;
import com.example.capstone3.Service.RentalOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/rental-offer")
public class RentalOfferController {

    private final RentalOfferService rentalOfferService;



    //1 -alaa
    @GetMapping("/for-shop/{rentalId}")
    public ResponseEntity<?> getAllRentalOffersWithDetail(@PathVariable Integer rentalId) {
        List<DetailRentalOfferDTO> rentalOffers = rentalOfferService.getAllRentalOffersWithDetail(rentalId);
        return ResponseEntity.status(200).body(rentalOffers);
    }

    //2-alaa
    @GetMapping("/{rentalOfferId}")
    public ResponseEntity<DetailRentalOfferDTO> getRentalOfferDetail(@PathVariable Integer rentalOfferId) {
        DetailRentalOfferDTO rentalOfferDetail = rentalOfferService.getRentalOfferDetail(rentalOfferId);
        return ResponseEntity.status(200).body(rentalOfferDetail);
    }

    //3-alaa
    @PostMapping("/accept-base-offer/{individualId}/{rentalId}")
    public ResponseEntity<?> acceptBaseOffer(@PathVariable Integer individualId, @PathVariable Integer rentalId) {
        rentalOfferService.acceptBaseOffer(individualId, rentalId);
        return ResponseEntity.status(200).body(new ApiResponse("The base offer has been accepted successfully."));
    }

    //4-alaa
    @PutMapping("/accept-or-reject-base/rental-id/{rentalId}/accepted/{accepted}")
    public ResponseEntity<?> acceptOrRejectBaseOfferByOwner(@PathVariable Integer rentalId, @PathVariable Boolean accepted) {
        DetailRentalOfferDTO rentalOfferDetail = rentalOfferService.acceptOrRejectBaseOfferByOwner(rentalId, accepted);
        return ResponseEntity.status(200).body(rentalOfferDetail);
    }

    //5-alaa
    @PostMapping("/start-offer/{individualId}/{rentalPropertyId}")
    public ResponseEntity<?> startRentalOffer(@PathVariable Integer individualId, @PathVariable Integer rentalPropertyId, @RequestParam Double individualProposedRent) {
        DetailRentalOfferDTO rentalOfferDetail = rentalOfferService.startRentalOffer(individualId, rentalPropertyId, individualProposedRent);
        return ResponseEntity.status(200).body(rentalOfferDetail);
    }

    //6-alaa
    @PutMapping("/owner-offer/{rentalOfferId}")
    public ResponseEntity<?> respondToRentalOfferByOwner(@PathVariable Integer rentalOfferId, @RequestParam Double ownerProposedRent) {
        DetailRentalOfferDTO rentalOfferDetail = rentalOfferService.respondToRentalOfferByOwner(rentalOfferId, ownerProposedRent);
        return ResponseEntity.status(200).body(rentalOfferDetail);
    }

    //7-alaa
    @PutMapping("/update-offer/rental-offer-id/{rentalOfferId}")
    public ResponseEntity<DetailRentalOfferDTO> updateRentalOfferByIndividual(@PathVariable Integer rentalOfferId, @RequestParam Double individualProposedRent) {
        DetailRentalOfferDTO rentalOfferDetail = rentalOfferService.updateRentalOfferByIndividual(rentalOfferId, individualProposedRent);
        return ResponseEntity.status(200).body(rentalOfferDetail);
    }


    //8-alaa
    @PutMapping("/individual-response/{rentalOfferId}")
    public ResponseEntity<DetailRentalOfferDTO> userAcceptOrRejectOwnerResponse(@PathVariable Integer rentalOfferId, @RequestParam Boolean accepted) {
        DetailRentalOfferDTO updatedOffer = rentalOfferService.userAcceptOrRejectOwnerResponse(rentalOfferId, accepted);
        return ResponseEntity.status(200).body(updatedOffer);
    }

    //9-alaa
    @PutMapping("/accept-or-reject-individual-offer/rental-offer-id/{rentalOfferId}")
    public ResponseEntity<DetailRentalOfferDTO> ownerAcceptOrRejectIndividualOffer(@PathVariable Integer rentalOfferId, @RequestParam Boolean accepted) {
        DetailRentalOfferDTO rentalOfferDetail = rentalOfferService.ownerAcceptOrRejectIndividualOffer(rentalOfferId, accepted);
        return ResponseEntity.ok(rentalOfferDetail);
    }

    //10-alaa
    @PutMapping("/accept-or-reject-offer/rental-offer-id/{rentalOfferId}")
    public ResponseEntity<DetailRentalOfferDTO> ownerAcceptOrRejectOffer(@PathVariable Integer rentalOfferId, @RequestParam Boolean accepted) {
        DetailRentalOfferDTO updatedOffer = rentalOfferService.ownerAcceptOrRejectOffer(rentalOfferId, accepted);
        return ResponseEntity.status(200).body(updatedOffer);
    }


    //11-alaa
    @GetMapping("/pending-by-owner/owner/{ownerId}")
    public ResponseEntity<?> getPendingOffersByOwner(
            @PathVariable Integer ownerId) {
        List<DetailRentalOfferDTO> pendingOffers = rentalOfferService.getPendingOffersByOwner(ownerId);
        return ResponseEntity.status(200).body(pendingOffers);
    }

    //12-alaa
    @GetMapping("/pending-by-individual/individual/{individualId}")
    public ResponseEntity<?> getPendingOffersByIndividual(@PathVariable Integer individualId) {
        List<DetailRentalOfferDTO> pendingOffers = rentalOfferService.getPendingOffersByIndividual(individualId);
        return ResponseEntity.status(200).body(pendingOffers);
    }

    //13-alaa
    @GetMapping("/rental/{rentalPropertyId}/owner/{ownerId}/offers")
    public ResponseEntity<List<DetailRentalOfferDTO>> getOffersForRentalPropertyByOwner(@PathVariable Integer rentalPropertyId, @PathVariable Integer ownerId) {
        List<DetailRentalOfferDTO> offers = rentalOfferService.getOffersForRentalPropertyByOwner(rentalPropertyId, ownerId);
        return ResponseEntity.status(200).body(offers);
    }







}
