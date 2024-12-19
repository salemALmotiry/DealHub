package com.example.capstone3.Repository;


import com.example.capstone3.Model.RentalOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalOfferRepository extends JpaRepository<RentalOffer, Integer> {

    RentalOffer findRentalOfferById(Integer rentalOfferId);

    List<RentalOffer> findRentalOfferByRentalId(Integer rentalId);

    @Query("SELECT O FROM RentalOffer O WHERE O.status='Pending by owner' and O.rental.owner.id=?1")
    List<RentalOffer> getPendingByOwner(Integer ownerId);

    @Query("SELECT O FROM RentalOffer O WHERE O.status='Pending by individual' and O.individual.id=?1")
    List<RentalOffer> getPendingByIndividual(Integer individualId);


    @Query("SELECT o FROM RentalOffer o WHERE o.rental.id =?1 AND o.id <>?2")
    List<RentalOffer> findOffersByRentalIdExcluding( Integer rentalPropertyId,  Integer acceptedOfferId);


    @Query("SELECT o FROM RentalOffer o WHERE o.rental.id =?1")
    List<RentalOffer> findOffersByRentalId(Integer rentalPropertyId);




}