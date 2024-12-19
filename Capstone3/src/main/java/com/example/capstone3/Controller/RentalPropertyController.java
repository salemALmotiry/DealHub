package com.example.capstone3.Controller;

import com.example.capstone3.ApiResponse.ApiResponse;
import com.example.capstone3.InDTO.RentalDTOIn;
import com.example.capstone3.Model.Rental;
import com.example.capstone3.OutDTO.RentalDTO;
import com.example.capstone3.Service.RentalPropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/rental")
public class RentalPropertyController {

    //by alaa


    private final RentalPropertyService rentalPropertyService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllRentalProperties(){
        return ResponseEntity.status(200).body(rentalPropertyService.getAllRental());
    }

    @GetMapping("/get-rental-property/{id}")
    public ResponseEntity<?> getRentalPropertyById(@PathVariable Integer id){
        return ResponseEntity.status(200).body(rentalPropertyService.getRentalById(id));
    }

    @PostMapping("/add/owner-id/{id}")
    public ResponseEntity<?> addRentalProperties(@PathVariable Integer id, @RequestBody @Valid RentalDTOIn rental){
        rentalPropertyService.add(id,rental);
        return ResponseEntity.status(200).body(new ApiResponse("Rental shop added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRentalProperties(@PathVariable Integer id, @RequestBody @Valid RentalDTOIn rental){
        rentalPropertyService.updateRental(id,rental);
        return ResponseEntity.status(200).body(new ApiResponse("Rental shop updated successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRentalProperties(@PathVariable Integer id){
        rentalPropertyService.deleteRental(id);
        return ResponseEntity.status(200).body(new ApiResponse("Rental shop deleted successfully"));
    }

    // Filter by city
    @GetMapping("/filter/city")
    public ResponseEntity<List<RentalDTO>> filterByCity(@RequestParam String city) {
        List<RentalDTO> rentals = rentalPropertyService.filterByCity(city);
        return ResponseEntity.status(200).body(rentals);
    }

    // Filter by location
    @GetMapping("/filter/location")
    public ResponseEntity<List<RentalDTO>> filterByLocation(@RequestParam String location) {
        List<RentalDTO> rentals = rentalPropertyService.filterByLocation(location);
        return ResponseEntity.status(200).body(rentals);
    }

    // Filter by unit type
    @GetMapping("/filter/unit-type")
    public ResponseEntity<List<RentalDTO>> filterByUnitType(@RequestParam String unitType) {
        List<RentalDTO> rentals = rentalPropertyService.filterByUnitType(unitType);
        return ResponseEntity.status(200).body(rentals);
    }








}
