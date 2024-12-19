package com.example.capstone3.Service;

import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.Model.Individual;
import com.example.capstone3.Model.Owner;
import com.example.capstone3.Model.Rental;
import com.example.capstone3.Model.RentalOffer;
import com.example.capstone3.OutDTO.DetailRentalOfferDTO;
import com.example.capstone3.Repository.IndividualRepository;
import com.example.capstone3.Repository.RentalOfferRepository;
import com.example.capstone3.Repository.RentalPropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalOfferService {

    //by alaa

    private final RentalOfferRepository rentalOfferRepository;
    private final RentalPropertyRepository rentalPropertyRepository;
    private final IndividualRepository individualRepository;

    // Retrieves all rental offers for a specific rental
    public List<DetailRentalOfferDTO> getAllRentalOffersWithDetail(Integer rentalId) {
        List<RentalOffer> rentalOffers = rentalOfferRepository.findRentalOfferByRentalId(rentalId);
        List<DetailRentalOfferDTO> rentalOfferDTOs = new ArrayList<>();
        for (RentalOffer rentalOffer : rentalOffers) {
            rentalOfferDTOs.add(convertToDetailRentalOfferDTO(rentalOffer));
        }
        return rentalOfferDTOs;
    }

    // Retrieves the details of a specific rental
    public DetailRentalOfferDTO getRentalOfferDetail(Integer rentalOfferId) {
        RentalOffer rentalOffer = rentalOfferRepository.findRentalOfferById(rentalOfferId);
        return convertToDetailRentalOfferDTO(rentalOffer);
    }

    /////////////////////***Offer process*//////////////////

    // Accepts the base offer made by an individual for a specific rental

    public DetailRentalOfferDTO acceptBaseOffer(Integer indiviualId,Integer rentalId){
        Rental rental = rentalPropertyRepository.findRentalById(rentalId);
        Individual individual = individualRepository.findIndividualById(indiviualId);
        if(rental == null){
            throw new ApiException("Rental not found to add offer");
        }
        if (individual == null){
            throw new ApiException("Individual not found to add offer");
        }
        if (rentalOfferRepository.findRentalOfferByRentalIdAndStatus(rental.getId(),"Accepted by owner")!=null) {
            throw new ApiException("There is accepted offer on this rental");
        }

        RentalOffer rentalOffer = new RentalOffer();
        rentalOffer.setRental(rental);
        rentalOffer.setRentalAmount(rental.getAnnulRent());
        rentalOffer.setIndividual(individual);
        rentalOffer.setIndividualAcceptedOwnerProposed(true);
        rentalOffer.setOwnerProposedRent(rental.getAnnulRent());
        rentalOffer.setStatus("Accepted by individual");
        rentalOffer.setCreatedAt(LocalDateTime.now());
        rentalOfferRepository.save(rentalOffer);
        return convertToDetailRentalOfferDTO(rentalOffer);
    }


// Accepts or rejects the base offer by the owner for a specific rental

    public DetailRentalOfferDTO acceptOrRejectBaseOfferByOwner(Integer rentalId,Boolean accepted){
        RentalOffer rentalOffer = rentalOfferRepository.findRentalOfferById(rentalId);
        Rental rental = rentalOffer.getRental();
        if(rentalOffer == null){
            throw new ApiException("Rental offer not found to accept base");
        }
        if(!"Accepted by individual".equals(rentalOffer.getStatus())){
            throw new ApiException("Rental offer is not in pending by owner status");
        }
        if (accepted) {
            rentalOffer.setStatus("Accepted by owner");
            rental.setAvaiilableStatus("Occupied");
            rentalOffer.setIndividualAcceptedOwnerProposed(true);
            rejectOtherOffers(rental.getId(),rentalOffer.getId());

        }else {
            rentalOffer.setStatus("Rejected by owner");
        }
        rentalOfferRepository.save(rentalOffer);
        return convertToDetailRentalOfferDTO(rentalOffer);
    }

    // Starts the negotiation process by an individual for a specific rental property

    public DetailRentalOfferDTO startRentalOffer(Integer individualId, Integer rentalProprtyId, Double individualProposedRent) {
        Rental rental = rentalPropertyRepository.findRentalById(rentalProprtyId);
        Individual individual = individualRepository.findIndividualById(individualId);
        if(rental == null){
            throw new ApiException("Rental not found to add offer");
        }
        if(individual == null){
            throw new ApiException("User not found to start rental offer");
        }

        if (rentalOfferRepository.findRentalOfferByRentalIdAndStatus(rental.getId(),"Accepted by owner")!=null) {
            throw new ApiException("There is accepted offer on this rental");
        }
        RentalOffer rentalOffer = new RentalOffer();
        rentalOffer.setRental(rental);
        rentalOffer.setRentalAmount(rental.getAnnulRent());
        rentalOffer.setIndividual(individual);
        rentalOffer.setIndividualProposedRent(individualProposedRent);
        rentalOffer.setIndividualAcceptedOwnerProposed(false);
        rentalOffer.setStatus("Pending by owner");
        rentalOffer.setCreatedAt(LocalDateTime.now());
        rentalOffer.setNegotiatedAt(LocalDateTime.now());
        rentalOfferRepository.save(rentalOffer);
        return convertToDetailRentalOfferDTO(rentalOffer);
    }

    // Responds to a rental offer by the owner with a new proposed rent amount

    public DetailRentalOfferDTO respondOfferByOwner(Integer rentalOfferId, Double ownerProposedRent) {
        RentalOffer rentalOffer = rentalOfferRepository.findRentalOfferById(rentalOfferId);
        if(!"Pending by owner".equals(rentalOffer.getStatus())){
            throw new ApiException("Rental offer is not in pending by owner status");
        }
        rentalOffer.setStatus("Pending by individual");
        rentalOffer.setOwnerProposedRent(ownerProposedRent);
        rentalOffer.setOwnerAcceptedIndividualProposal(false);
        rentalOffer.setNegotiatedAt(LocalDateTime.now());
        rentalOfferRepository.save(rentalOffer);
        return convertToDetailRentalOfferDTO(rentalOffer);
    }

    // Updates the rental offer by the individual with a new proposed rent amount

    public DetailRentalOfferDTO responseOfferByIndividual(Integer rentalOfferId, Double individualProposedRent) {
        RentalOffer rentalOffer = rentalOfferRepository.findRentalOfferById(rentalOfferId);
        if(rentalOffer == null){
            throw new ApiException("Rental offer not found to update offer");
        }
        if (!rentalOffer.getStatus().equals("Pending by individual")) {
            throw new ApiException("Rental offer is not in pending by individual status");
        }

        rentalOffer.setStatus("Pending by owner");
        rentalOffer.setIndividualProposedRent(individualProposedRent);
        rentalOfferRepository.save(rentalOffer);
        return convertToDetailRentalOfferDTO(rentalOffer);
    }


    // Accepts or rejects the owner's response by the individual

    public DetailRentalOfferDTO individualAcceptOrRejectOffer(Integer rentalOfferId, Boolean Accepted) {
        RentalOffer rentalOffer = rentalOfferRepository.findRentalOfferById(rentalOfferId);
        Individual individual = individualRepository.findIndividualById(rentalOffer.getIndividual().getId());
        if(rentalOffer == null){
            throw new ApiException("Rental offer not found to start rental offer");
        }
        if(individual == null){
            throw new ApiException("User not found");
        }
        if(!"Pending by individual".equals(rentalOffer.getStatus())){
            throw new ApiException("Rental offer is not in pending by individual status");
        }
        if(Accepted){
            rentalOffer.setStatus("Accepted by individual");
            rentalOffer.setIndividualAcceptedOwnerProposed(true);
        }else {
            rentalOffer.setStatus("Rejected by individual");
            rentalOffer.setIndividualAcceptedOwnerProposed(false);
        }

        rentalOfferRepository.save(rentalOffer);
        return convertToDetailRentalOfferDTO(rentalOffer);
    }

    // Final acceptance or rejection of the negotiation in favor of the owner's offer

    public DetailRentalOfferDTO ownerAcceptOrRejectOffer(Integer rentalOfferId, Boolean accepted) {
        RentalOffer rentalOffer = rentalOfferRepository.findRentalOfferById(rentalOfferId);
        Rental rental = rentalOffer.getRental();            rejectOtherOffers(rental.getId(),rentalOffer.getId());

        if(!"Accepted by individual".equals(rentalOffer.getStatus())&&!"Pending by owner".equals(rentalOffer.getStatus())){
            throw new ApiException("Invalid status ");
        }
        if (accepted) {
            rentalOffer.setStatus("Accepted by owner");
            rental.setAvaiilableStatus("Occupied");
            rentalOffer.setOwnerAcceptedIndividualProposal(true);
            rentalOffer.setRentalAmount(rentalOffer.getIndividualProposedRent());
            rentalOffer.setRentalAmount(rentalOffer.getOwnerProposedRent());
        }else {
            rentalOffer.setStatus("Rejected");
        }

        rentalOffer.setNegotiatedAt(LocalDateTime.now());
        rentalOfferRepository.save(rentalOffer);
        return convertToDetailRentalOfferDTO(rentalOffer);
    }


    // Rejects other offers for the same rental property except the accepted offer

    private void rejectOtherOffers(Integer rentalPropertyId, Integer acceptedOfferId) {
        List<RentalOffer> otherOffers = rentalOfferRepository.findOffersByRentalIdExcluding(rentalPropertyId, acceptedOfferId);
        for (RentalOffer offer : otherOffers) {
            if (!"Rejected".equals(offer.getStatus())) {
                offer.setStatus("Rejected by owner");
                rentalOfferRepository.save(offer);
            }
        }
    }


    // Retrieves all pending offers by the owner

    public List<DetailRentalOfferDTO> getPendingOffersByOwner(Integer ownerId) {
        List<RentalOffer> rentalOffers = rentalOfferRepository.getPendingByOwner(ownerId);
        List<DetailRentalOfferDTO> offerDTOs = new ArrayList<>();

        if (rentalOffers.isEmpty()){
            throw new ApiException("No pending offers found");
        }
        for (RentalOffer offer : rentalOffers) {
            offerDTOs.add(convertToDetailRentalOfferDTO(offer));
        }
        return offerDTOs;
    }



    // Retrieves pending rental offers for an individual, throwing an exception if none are found.

    public List<DetailRentalOfferDTO> getPendingOffersByIndividual(Integer individualId) {
        List<RentalOffer> rentalOffers = rentalOfferRepository.getPendingByIndividual(individualId);
        List<DetailRentalOfferDTO> offerDTOs = new ArrayList<>();

        if (rentalOffers.isEmpty()){
            throw new ApiException("No pending offers found");
        }
        for (RentalOffer offer : rentalOffers) {
            offerDTOs.add(convertToDetailRentalOfferDTO(offer));
        }
        return offerDTOs;
    }

    // Retrieves rental offers for a property owned by a specific owner, ensuring ownership validation.

    public List<DetailRentalOfferDTO> getOffersForRentalPropertyByOwner(Integer rentalPropertyId, Integer ownerId) {

        Rental rental = rentalPropertyRepository.findRentalById(rentalPropertyId);
        if (rental == null) {
            throw new ApiException("Rental property not found.");
        }
        if (!rental.getOwner().getId().equals(ownerId)) {
            throw new ApiException("Unauthorized access: This rental property does not belong to the owner.");
        }

        List<RentalOffer> rentalOffers = rentalOfferRepository.findOffersByRentalId(rentalPropertyId);
        List<DetailRentalOfferDTO> offerDTOs = new ArrayList<>();

        for (RentalOffer offer : rentalOffers) {
            offerDTOs.add(convertToDetailRentalOfferDTO(offer));
        }

        return offerDTOs;
    }


    public DetailRentalOfferDTO convertToDetailRentalOfferDTO(RentalOffer rentalOffer) {
        Rental rental = rentalOffer.getRental();
        Individual individual = rentalOffer.getIndividual();
        Owner owner = rental.getOwner();
        return new DetailRentalOfferDTO(
                rentalOffer.getId(),
                rental.getName(),
                rentalOffer.getRentalAmount(),
                rentalOffer.getStatus(),
                individual.getFullName(),
                rentalOffer.getIndividualAcceptedOwnerProposed(),
                rentalOffer.getIndividualProposedRent(),owner.getFullName(),
                rentalOffer.getOwnerAcceptedIndividualProposal(),
                rentalOffer.getOwnerProposedRent(),
                rentalOffer.getCreatedAt(),
                rentalOffer.getNegotiatedAt()
        );
    }



}
