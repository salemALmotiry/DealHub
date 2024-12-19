package com.example.capstone3.Repository;

import com.example.capstone3.Model.StoreContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreContractRepository extends JpaRepository<StoreContract, Integer> {

    StoreContract findStoreContractById(Integer storeContractId);

    List<StoreContract> findStoreContractByIndividualId(Integer individualId);

    List<StoreContract> findStoreContractByOwnerId(Integer ownerId);

    StoreContract findStoreContractByStoreId(Integer id);
}
