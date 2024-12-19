package com.example.capstone3.Repository;


import com.example.capstone3.Model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Done by salem
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Owner findOwnerById(Integer ownerId);
}
