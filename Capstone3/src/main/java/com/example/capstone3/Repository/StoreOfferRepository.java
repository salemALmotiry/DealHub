package com.example.capstone3.Repository;

import com.example.capstone3.Model.StoreOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOfferRepository  extends JpaRepository <StoreOffer, Integer>{

    StoreOffer findStoreOfferById(Integer storeOfferId);
    List<StoreOffer> findStoreOfferByStoreId(Integer storeId);

    StoreOffer findStoreOfferByIdAndStoreId(Integer storeOfferId,Integer storeId);

    List<StoreOffer> findStoreOfferByIndividualIdAndStoreId(Integer individualId,Integer storeId);

    @Query("select s from StoreOffer s where s.individual.id =?1 and s.store.id = ?2 and s.status <> 'Rejected'")
    StoreOffer getStoreOfferActive(Integer individualId,Integer storeId);

    List<StoreOffer> findStoreOfferByIndividualIdAndStatus(Integer individualId,String status);


}
