package com.example.capstone3.Service;

import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.InDTO.RentalContractDTOIn;
import com.example.capstone3.Model.*;
import com.example.capstone3.OutDTO.DetailRentalOfferDTO;
import com.example.capstone3.OutDTO.RentalContractDTO;
import com.example.capstone3.Repository.RentalContractRepository;
import com.example.capstone3.Repository.RentalOfferRepository;
import com.example.capstone3.Repository.RentalPropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalContractService {

    //by alaa

    private final RentalContractRepository rentalContractRepository;
    private final RentalOfferRepository rentalOfferRepository;
    private final RentalPropertyRepository rentalPropertyRepository;

    // Creates a new rental contract
    public void createRentalContract(RentalContractDTOIn rentalContract) {
        RentalOffer rentalOffer = rentalOfferRepository.findRentalOfferById(rentalContract.getRentalOfferId());
        Rental rental = rentalPropertyRepository.findRentalById(rentalContract.getRentalId());
        if (rentalOffer == null) {
            throw new ApiException("Rental offer not found");
        }
        if (rentalContract.getIndividualId()!=rentalOffer.getIndividual().getId()){
            throw new ApiException("Rental contract individual id mismatch");
        }
        if (rentalContract.getOwnerId()!=rental.getOwner().getId()){
            throw new ApiException("Rental contract owner id mismatch");
        }
        if (!rentalOffer.getStatus().equals("Accepted by owner")) {
            throw new ApiException("Rental offer not accepted by owner. Cannot create contract ");
        }
        if (!rentalOffer.getRentalContracts().isEmpty()){
            throw new ApiException("There is an active contract");
        }

        RentalContract contract = new RentalContract();
        contract.setContractNumber(rentalContract.getContractNumber());
        contract.setContractStatus("Active");
        contract.setStartDate(rentalContract.getStartDate());
        contract.setEndDate(rentalContract.getEndDate());
        contract.setSealingDate(LocalDate.now());
        contract.setTotalContractAmount(rentalOffer.getRentalAmount());
        contract.setNubmerElectricityBill(rentalContract.getNubmerElectricityBill());
        contract.setNumberWaterBill(rentalContract.getNumberWaterBill());
        contract.setOwner(rental.getOwner());
        contract.setIndividual(rentalOffer.getIndividual());
        contract.setRental(rental);
        contract.setRentalOffer(rentalOffer);
        rentalContractRepository.save(contract);
    }


// Retrieves a rental contract by its rental offer ID , to check if there contract for this offer.

    public RentalContractDTO getRentalContractByRentalOfferId(Integer rentalOfferId) {
        RentalContract rentalContract = rentalContractRepository.findRentalContractById(rentalOfferId);
        if (rentalContract == null) {
            throw new ApiException("Rental contract not found");
        }
        return convertRentalContractToDTO(rentalContract);
    }


    // Updates the status of all active rental contracts to "Expired" if their end date is before the current date.
    public void updateExpiredContract() {
        List<RentalContract> activeContracts = rentalContractRepository.findRentalContractsByContractStatus("Active");
        LocalDate now = LocalDate.now();
        for (RentalContract activeContract : activeContracts) {
            if (activeContract.getEndDate().isBefore(now)) {
                activeContract.setContractStatus("Expired");
                if (activeContract.getRental()!=null) {
                    activeContract.getRental().setAvaiilableStatus("Available");
                }
                rentalContractRepository.save(activeContract);
            }
        }
    }


    public List<RentalContractDTO> getAllRentalContractOfIndividualAndStatus(Integer individualId, String status) {
        List<RentalContract> contracts = rentalContractRepository.getContractsByIndividualAndStatus(individualId,status);
        List<RentalContractDTO> contractDTOS= new ArrayList<>();
        for (RentalContract contract : contracts) {
            contractDTOS.add(convertRentalContractToDTO(contract));
        }
        return contractDTOS;
    }

    public List<RentalContractDTO> getAllRentalContractOfOwnerAndStatus(Integer ownerId, String status) {
        List<RentalContract> contracts = rentalContractRepository.getContractsByOwnerAndStatus(ownerId, status);
        List<RentalContractDTO> contractDTOS= new ArrayList<>();
        for (RentalContract contract : contracts) {
            contractDTOS.add(convertRentalContractToDTO(contract));
        }
        return contractDTOS;
    }

    public RentalContractDTO convertRentalContractToDTO(RentalContract contract) {
        Rental rental = contract.getRental();
        RentalOffer rentalOffer = contract.getRentalOffer();
        Individual individual = contract.getIndividual();
        Owner owner = contract.getOwner();
        RentalContractDTO rentalContractDTO = new RentalContractDTO(
                contract.getContractNumber(),
                contract.getContractStatus(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getSealingDate(),
                rentalOffer.getRentalAmount(),
                contract.getNubmerElectricityBill(),
                contract.getNumberWaterBill(),
                owner.getId(),
                owner.getFullName(),
                individual.getId(),
                individual.getFullName(),
                rentalOffer.getId(),
                rental.getId(),
                rental.getName(),
                contract.getCreatedAt()
        );
        return rentalContractDTO;

    }




}
