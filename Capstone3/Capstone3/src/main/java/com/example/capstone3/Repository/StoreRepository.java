package com.example.capstone3.Repository;

import com.example.capstone3.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    Store findStoreById(Integer storeId);


}
