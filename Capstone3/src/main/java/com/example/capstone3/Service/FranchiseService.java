package com.example.capstone3.Service;

import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.Model.Franchise;
import com.example.capstone3.Model.FranchiseContracts;
import com.example.capstone3.Model.FranchiseOffer;
import com.example.capstone3.OutDTO.FranchiseDTO;
import com.example.capstone3.Repository.FranchiseContractRepository;
import com.example.capstone3.Repository.FranchiseOfferRepository;
import com.example.capstone3.Repository.FranchiseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class  FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseOfferService franchiseOfferService;
    private final FranchiseContractRepository franchiseContractRepository;
    private final FranchiseOfferRepository franchiseOfferRepository;


    public void add(Franchise franchise){

        franchiseRepository.save(franchise);

    }

    public void update(Integer id,Franchise franchise){
        Franchise oldFranchise = franchiseRepository.findFranchiseById(id);

        oldFranchise.setBrandName(franchise.getBrandName());
        oldFranchise.setLicenseNumber(franchise.getLicenseNumber());
        oldFranchise.setContractDuration(franchise.getContractDuration());
        oldFranchise.setInvestmentAmount(franchise.getInvestmentAmount());
        oldFranchise.setFranchiseFee(franchise.getFranchiseFee());
        oldFranchise.setNumberOfBranches(franchise.getNumberOfBranches());
        oldFranchise.setCountryOfOrigin(franchise.getCountryOfOrigin());
        oldFranchise.setFranchiseType(franchise.getFranchiseType());
        oldFranchise.setSector(franchise.getSector());
        oldFranchise.setOngoingAdminFees(franchise.getOngoingAdminFees());
        oldFranchise.setDescription(franchise.getDescription());
        oldFranchise.setSupportDetails(franchise.getSupportDetails());

    }

    public void delete(Integer id){
        FranchiseContracts franchiseContracts = franchiseContractRepository.findFranchiseContractsByFranchiseIdAndStatus(id, "Active");
        if (franchiseContracts != null) {
            throw new ApiException("Cannot delete franchise with active contracts");
        }

        Franchise franchise = franchiseRepository.findFranchiseById(id);

        if (franchise == null) {
            throw new ApiException("Franchise not found");
        }
        franchiseRepository.delete(franchise);
    }


    public List<FranchiseDTO> franchises(){
        List<FranchiseDTO> franchiseDTOS = franchiseRepository.findAll().stream().map(this::mapToDto).toList();

        return franchiseDTOS;
    }



    public FranchiseDTO mapToDto(Franchise franchise) {
        if (franchise == null) {
            return null;
        }
        FranchiseDTO dto = new FranchiseDTO();
        dto.setBrandName(franchise.getBrandName());
        dto.setLicenseNumber(franchise.getLicenseNumber());
        dto.setContractDuration(franchise.getContractDuration());
        dto.setInvestmentAmount(franchise.getInvestmentAmount());
        dto.setFranchiseFee(franchise.getFranchiseFee());
        dto.setNumberOfBranches(franchise.getNumberOfBranches());
        dto.setCountryOfOrigin(franchise.getCountryOfOrigin());
        dto.setFranchiseType(franchise.getFranchiseType());
        dto.setSector(franchise.getSector());
        dto.setOngoingAdminFees(franchise.getOngoingAdminFees());
        dto.setDescription(franchise.getDescription());
        dto.setSupportDetails(franchise.getSupportDetails());
        return dto;
    }



}
