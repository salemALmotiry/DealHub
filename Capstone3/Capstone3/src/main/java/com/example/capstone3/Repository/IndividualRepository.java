package com.example.capstone3.Repository;

import com.example.capstone3.Model.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Done by Salem
public interface IndividualRepository extends JpaRepository<Individual, Integer> {
    Individual findIndividualById(Integer individualId);
}
