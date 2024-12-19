package com.example.capstone3.Service;


import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.InDTO.StoreDTOIn;
import com.example.capstone3.Model.Owner;
import com.example.capstone3.Model.Store;
import com.example.capstone3.Model.StoreContract;
import com.example.capstone3.Model.StoreOffer;
import com.example.capstone3.OutDTO.StoreDTO;
import com.example.capstone3.Repository.OwnerRepository;
import com.example.capstone3.Repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final StoreOfferService storeOfferService;


    public List<StoreDTO> getAllStores(){

        return storeRepository.findAll().stream().map(this::convertStoreDto).collect(Collectors.toList());
    }
    public StoreDTO getStoreById(Integer id){
        Store store = storeRepository.findStoreById(id);
        return convertStoreDto(store);
    }

    public void addStore(StoreDTOIn storeDTOIn){
        Owner owner = ownerRepository.findOwnerById(storeDTOIn.getOwner_id());
        if (owner == null) throw new ApiException("Error: owner not found");

        Store store = new Store();

        store.setStoreName(storeDTOIn.getStoreName());
        store.setLocation(storeDTOIn.getLocation());
        store.setWithEquipment(storeDTOIn.getWithEquipment());
        store.setStoreType(storeDTOIn.getStoreType());
        store.setNumberOfBranches(storeDTOIn.getNumberOfBranches());
        store.setOriginalPrice(storeDTOIn.getOriginalPrice());
        store.setDescription(storeDTOIn.getDescription());
        store.setOwner(owner);

        storeRepository.save(store);

    }

    public void updateStore(Integer id,StoreDTOIn storeDTOIn){
        Store store = storeRepository.findStoreById(id);
        if (store == null) throw new ApiException("Error: store not found");
        if (!store.getStoreContracts().isEmpty()) throw new ApiException("Error : store is sold you can't update it" );
        if (store.getStatus().equalsIgnoreCase("Active")) throw new ApiException("Error : store is Active you can't update it, if you want update make it Inactive to make changes" );
        store.setStoreName(storeDTOIn.getStoreName());
        store.setLocation(storeDTOIn.getLocation());
        store.setWithEquipment(storeDTOIn.getWithEquipment());
        store.setStoreType(storeDTOIn.getStoreType());
        store.setNumberOfBranches(storeDTOIn.getNumberOfBranches());
        store.setOriginalPrice(storeDTOIn.getOriginalPrice());
        store.setDescription(storeDTOIn.getDescription());
        store.setUpdatedAt(java.time.LocalDateTime.now());
        storeRepository.save(store);
    }


    public void deleteStore(Integer id){
        Store store = storeRepository.findStoreById(id);
        if (store == null) throw new ApiException("Error: store not found");
        if (!store.getStoreContracts().isEmpty()) throw new ApiException("Error : store is sold you can't update it" );

        // checking there is offer active before male the delete -- by basil
        Set<StoreOffer> storeOffers = store.getStoreOffers()
                .stream()
                .filter(storeOffer -> !storeOffer.getStatus().equalsIgnoreCase("Rejected"))
                .collect(Collectors.toSet());
        if (!storeOffers.isEmpty()) throw new ApiException("Error: there offer active in this store");

        storeRepository.deleteById(id);
    }


    // active the store to allow the individual make offer on store and prevent the owner make changes to store -- by basil
    public void activeStore(Integer id){
        Store store = storeRepository.findStoreById(id);
        if (store == null) throw new ApiException("Error: store not found");
        if (!store.getStoreContracts().isEmpty()) throw new ApiException("Error : store is sold you can't update status" );
        store.setStatus("Active");
        storeRepository.save(store);
    }
    // inactive the store reject all active offers and prevent the individual to make offer and allow the owner make changes to store -- by basil
    public void inactiveStore(Integer id){
        Store store = storeRepository.findStoreById(id);
        if (store == null) throw new ApiException("Error: store not found");
        if (!store.getStoreContracts().isEmpty()) throw new ApiException("Error : store is sold you can't update status" );

        store.setStatus("Inactive");
        storeRepository.save(store);
        storeOfferService.rejectAllOffers(store.getStoreOffers());
    }
    public StoreDTO convertStoreDto(Store store){

        Owner owner = store.getOwner();

        return new StoreDTO( store.getId(),store.getStoreName(),store.getLocation(),store.getWithEquipment(),store.getStoreType(),store.getNumberOfBranches(),store.getOriginalPrice(),store.getDescription(),owner.getFullName(),store.getStatus());
    }
}
