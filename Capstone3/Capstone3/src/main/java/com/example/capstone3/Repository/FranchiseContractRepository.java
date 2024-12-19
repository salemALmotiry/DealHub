package com.example.capstone3.Repository;

import com.example.capstone3.Model.FranchiseContracts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Done by Salem
public interface  FranchiseContractRepository extends JpaRepository<FranchiseContracts, Integer> {

    FranchiseContracts findFranchiseContractById(Integer id);

    FranchiseContracts findFranchiseContractsByFranchiseIdAndStatus(Integer franchiseId, String status);


    List<FranchiseContracts> findFranchiseContractsByStatus(String status);

    List<FranchiseContracts> findFranchiseContractsByIndividualIdAndStatus(Integer id,String status);

    List<FranchiseContracts> findFranchiseContractsByOwnerIdAndStatus(Integer id,String status);
}
