package com.example.capstone3.Repository;

import com.example.capstone3.Model.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Done by Salem
public interface FranchiseRepository extends JpaRepository<Franchise,Integer> {

    Franchise findFranchiseById(Integer id);

    Franchise findFranchiseByIdAndOwnerId(Integer id, Integer ownerId);

}
