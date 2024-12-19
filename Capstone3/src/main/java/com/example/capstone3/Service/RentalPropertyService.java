package com.example.capstone3.Service;

import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.InDTO.RentalDTOIn;
import com.example.capstone3.Model.Owner;
import com.example.capstone3.Model.Rental;
import com.example.capstone3.Model.RentalContract;
import com.example.capstone3.OutDTO.RentalDTO;
import com.example.capstone3.Repository.OwnerRepository;
import com.example.capstone3.Repository.RentalContractRepository;
import com.example.capstone3.Repository.RentalPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalPropertyService {

    //by alaa

    private final RentalPropertyRepository rentalPropertyRepository;
    private final OwnerRepository ownerRepository;
    private final RentalContractRepository rentalContractRepository;


    //get all
    public List<RentalDTO> getAllRental(){
        List<Rental> rentals = rentalPropertyRepository.findAll();
        List<RentalDTO> rentalDTOs = new ArrayList<>();
        for (Rental rental : rentals) {
            rentalDTOs.add(convertRentalDto(rental));
        }
        return rentalDTOs;
    }

    //get specific rental properties
    public RentalDTO getRentalById(Integer rentalId){
        Rental rental = rentalPropertyRepository.findRentalById(rentalId);
        return convertRentalDto(rental);
    }

    //insert
    public void add(Integer owner_id, RentalDTOIn rentalDTOIn){
        Owner owner = ownerRepository.findOwnerById(owner_id);

        if (owner==null){
            throw new ApiException("Owner not found");
        }
        Rental rental = new Rental();

        rental.setCreatedAt(LocalDate.now());
        rental.setOwner(owner);
        rental.setName(rentalDTOIn.getName());
        rental.setAvaiilableStatus(rentalDTOIn.getAvaiilableStatus());
        rental.setCity(rentalDTOIn.getCity());
        rental.setLocation(rentalDTOIn.getLocation());
        rental.setAnnulRent(rentalDTOIn.getAnnulRent());
        rental.setRentalType(rentalDTOIn.getRentalType());
        rental.setUnitType(rentalDTOIn.getUnitType());
        rental.setFloor(rentalDTOIn.getFloor());
        rental.setUnitAge(rentalDTOIn.getUnitAge());
        rental.setUnitNumber(rentalDTOIn.getUnitNumber());
        rental.setUnitArea(rentalDTOIn.getUnitArea());

        rentalPropertyRepository.save(rental);
    }

    //update

    public void updateRental(Integer id, RentalDTOIn rentalDTOIn){
        Rental rental = rentalPropertyRepository.findRentalById(id);
        if (rental ==null){
            throw new ApiException("Rental Properties not found");
        }
        RentalContract rentalContract = rentalContractRepository.findRentalContractByRentalId(rental.getId());
        if (rentalContract.getContractStatus().equals("Active")){
            throw new ApiException("There is an active contract for the property");
        }

        rental.setName(rentalDTOIn.getName());
        rental.setAvaiilableStatus(rentalDTOIn.getAvaiilableStatus());
        rental.setCity(rentalDTOIn.getCity());
        rental.setLocation(rentalDTOIn.getLocation());
        rental.setRentalType(rentalDTOIn.getRentalType());
        rental.setUnitType(rentalDTOIn.getUnitType());
        rental.setFloor(rentalDTOIn.getFloor());
        rental.setUnitAge(rentalDTOIn.getUnitAge());
        rental.setUnitNumber(rentalDTOIn.getUnitNumber());
        rental.setUnitArea(rentalDTOIn.getUnitArea());
        rental.setUpdatedAt(LocalDate.now());

        rentalPropertyRepository.save(rental);
    }

    //delete
    public void deleteRental(Integer id){
        Rental rental = rentalPropertyRepository.findRentalById(id);
        RentalContract rentalContract = rentalContractRepository.findRentalContractByRentalId(rental.getId());

        if (rentalContract.getContractStatus().equals("Active")){
            throw new ApiException("There is an active contract for the property");
        }
        if (rental==null){
            throw new ApiException("Rental Properties not found");
        }
        rentalPropertyRepository.delete(rental);
    }


    // filter by city
    public List<RentalDTO> filterByCity(String city) {
        List<Rental> rentals = rentalPropertyRepository.findByCity(city);
        return convertToRentalPropertyDTOs(rentals);
    }

    // filter by location
    public List<RentalDTO> filterByLocation(String location) {
        List<Rental> rentals = rentalPropertyRepository.findByLocation(location);
        return convertToRentalPropertyDTOs(rentals);
    }

    // filter by unit type
    public List<RentalDTO> filterByUnitType(String unitType) {
        List<Rental> rentals = rentalPropertyRepository.findByUnitType(unitType);
        return convertToRentalPropertyDTOs(rentals);
    }


    private List<RentalDTO> convertToRentalPropertyDTOs(List<Rental> rentals) {
        List<RentalDTO> propertyDTOs = new ArrayList<>();
        for (Rental rental : rentals) {
            propertyDTOs.add( new RentalDTO(null,rental.getOwner().getFullName(),rental.getName(),rental.getAnnulRent(),rental.getAvaiilableStatus(),rental.getCity(),rental.getLocation(), rental.getRentalType(), rental.getUnitType(),rental.getFloor(),rental.getUnitAge(),rental.getUnitNumber(),rental.getUnitArea()));
        }
        return propertyDTOs;
    }

    public RentalDTO convertRentalDto(Rental rental){

        Owner owner = rental.getOwner();

        return new RentalDTO(rental.getId(), owner.getFullName(),rental.getName(),rental.getAnnulRent(),rental.getAvaiilableStatus(),rental.getCity(),rental.getLocation(), rental.getRentalType(), rental.getUnitType(),rental.getFloor(),rental.getUnitAge(),rental.getUnitNumber(),rental.getUnitArea());
    }


}
