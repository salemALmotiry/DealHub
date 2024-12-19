package com.example.capstone3.Service;

import com.example.capstone3.Model.Individual;
import com.example.capstone3.Model.Owner;
import com.example.capstone3.Model.StoreContract;
import com.example.capstone3.OutDTO.StoreContractDTO;
import com.example.capstone3.Repository.StoreContractRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreContractService {
    private final StoreContractRepository storeContractRepository;

    // get all store contract by individual - by basil
    public List<StoreContractDTO> getAllStoreContractByIndividual(Integer individualId) {

        List<StoreContract> storeContracts = storeContractRepository.findStoreContractByIndividualId(individualId);

        return storeContracts.stream().map(this::convertStoreContractToDTO).collect(Collectors.toList());
    }
    // get all store contract by owner - by basil
    public List<StoreContractDTO> getAllStoreContractByOwner(Integer ownerId) {

        List<StoreContract> storeContracts = storeContractRepository.findStoreContractByOwnerId(ownerId);

        return storeContracts.stream().map(this::convertStoreContractToDTO).collect(Collectors.toList());
    }

    public StoreContractDTO convertStoreContractToDTO(StoreContract storeContract) {

        return new StoreContractDTO(storeContract.getStoreName(),storeContract.getLocation(),storeContract.getWithEquipment(),storeContract.getStoreType(),
                storeContract.getNumberOfBranches(),storeContract.getDescription(),storeContract.getAgreedPrice(),storeContract.getContractDate(),
                storeContract.getIndividual().getFullName(),storeContract.getOwner().getFullName());
    }
}
