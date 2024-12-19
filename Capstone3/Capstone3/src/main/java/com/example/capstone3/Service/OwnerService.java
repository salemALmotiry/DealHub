package com.example.capstone3.Service;

import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.InDTO.FranchiseOfferIn;
import com.example.capstone3.Model.*;
import com.example.capstone3.OutDTO.FranchiseOfferDTO;
import com.example.capstone3.Repository.FranchiseContractRepository;
import com.example.capstone3.Repository.FranchiseOfferRepository;
import com.example.capstone3.Repository.FranchiseRepository;
import com.example.capstone3.Repository.OwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor

// Done by Salem
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final FranchiseOfferRepository franchiseOfferRepository;
    private final FranchiseContractRepository franchiseContractRepository;
    private final FranchiseRepository franchiseRepository;

    public void add(Owner owner){
        ownerRepository.save(owner);
    }

    public  void update(Integer id,Owner owner){
        Owner oldOwner = ownerRepository.findOwnerById(id);

        if (oldOwner == null){
            throw new ApiException("Owner not found");
        }

        oldOwner.setEmail(owner.getEmail());
        oldOwner.setFullName(owner.getFullName());
        oldOwner.setPhoneNumber(owner.getPhoneNumber());
        ownerRepository.save(oldOwner);
    }




    public FranchiseOfferDTO mapToDto(FranchiseOffer franchiseOffer) {
        if (franchiseOffer == null) {
            return null;
        }

        FranchiseOfferDTO dto = new FranchiseOfferDTO();

        // Mapping basic fields
        dto.setFranchiseName(franchiseOffer.getFranchise().getBrandName());
        dto.setOfferedBy("Me");
        dto.setOfferedFee(franchiseOffer.getOfferedFee());
        dto.setCounterOfferFee(franchiseOffer.getCounterOfferFee());
        dto.setContractDuration(Integer.valueOf(franchiseOffer.getContractDuration()));
        dto.setCounterContractDuration(franchiseOffer.getCounterContractDuration());
        dto.setInvestmentAmount(franchiseOffer.getInvestmentAmount());
        dto.setCounterInvestmentAmount(franchiseOffer.getCounterInvestmentAmount());
        dto.setOngoingAdminFees(franchiseOffer.getOngoingAdminFees());
        dto.setCounterOngoingAdminFees(franchiseOffer.getCounterOngoingAdminFees());
        dto.setSameDeal(franchiseOffer.isSameDeal());
        dto.setStatus(franchiseOffer.getStatus());

        return dto;
    }


}
