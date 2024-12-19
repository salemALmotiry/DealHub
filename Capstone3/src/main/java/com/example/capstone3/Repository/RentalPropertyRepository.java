package com.example.capstone3.Repository;


import com.example.capstone3.Model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalPropertyRepository extends JpaRepository<Rental, Integer> {

    //by alaa

    Rental findRentalById(Integer rentalId);

    // البحث بناءً على البلد
    List<Rental> findByCity(String city);

    // البحث بناءً على الموقع
    List<Rental> findByLocation(String location);

    // البحث بناءً على نوع الوحدة
    List<Rental> findByUnitType(String unitType);

}
