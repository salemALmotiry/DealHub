package com.example.capstone3.Repository;


import com.example.capstone3.Model.RentalContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalContractRepository extends JpaRepository<RentalContract, Integer> {

    //by alaa

    RentalContract findRentalContractById(Integer rentalContractId);

    RentalContract findRentalContractByRentalId(Integer rentalId );

    List<RentalContract> findRentalContractsByContractStatus(String contractStatus);

    @Query("select c from RentalContract c where c.individual.id=?1 and c.contractStatus=?2")
    List<RentalContract> getContractsByIndividualAndStatus(Integer individualId, String contractStatus);


    @Query("select c from RentalContract c where c.owner.id=?1 and c.contractStatus=?2")
    List<RentalContract> getContractsByOwnerAndStatus(Integer ownerId, String contractStatus);

    RentalContract findRentalContractByRentalOfferId(Integer rentalOfferId);


}
