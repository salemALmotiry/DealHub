package com.example.capstone3.Service;

import com.example.capstone3.Model.FranchiseContracts;
import com.example.capstone3.OutDTO.FranchiseContractDTO;
import com.example.capstone3.Repository.FranchiseContractRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


// Done by salem
@Service
@AllArgsConstructor
public class  FranchiseContractService {

    private final FranchiseContractRepository franchiseContractRepository;


    public List<FranchiseContractDTO> getActiveContracts(Integer individualId) {

        List<FranchiseContracts> contracts = franchiseContractRepository.findFranchiseContractsByIndividualIdAndStatus(individualId, "Active");
        return contracts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<FranchiseContractDTO> getExpiredContracts(Integer individualId) {

        List<FranchiseContracts> contracts = franchiseContractRepository
                .findFranchiseContractsByIndividualIdAndStatus(individualId, "Expired");
        return contracts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

       }


    public List<FranchiseContractDTO> getActiveContractsOwner(Integer ownerId) {

        List<FranchiseContracts> contracts = franchiseContractRepository
                .findFranchiseContractsByOwnerIdAndStatus(ownerId, "Active");
        return contracts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<FranchiseContractDTO> getExpiredContractsOwner(Integer ownerId) {

        List<FranchiseContracts> contracts = franchiseContractRepository
                .findFranchiseContractsByOwnerIdAndStatus(ownerId, "Expired");
        return contracts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public FranchiseContractDTO toDTO(FranchiseContracts contract) {
        FranchiseContractDTO dto = new FranchiseContractDTO();
        dto.setFranchiseeName(contract.getFranchiseeName());
        dto.setFranchisorName(contract.getFranchisorName());
        dto.setFranchisorPhone(contract.getFranchisorPhone());
        dto.setSecondPartyName(contract.getSecondPartyName());
        dto.setSecondPartyPhone(contract.getSecondPartyPhone());
        dto.setContractDuration(contract.getContractDuration());
        dto.setInvestmentAmount(contract.getInvestmentAmount());
        dto.setOngoingAdminFees(contract.getOngoingAdminFees());
        dto.setAgreedFee(contract.getAgreedFee());
        return dto;
    }
}
