package com.example.capstone3.Controller;


import com.example.capstone3.ApiResponse.ApiResponse;
import com.example.capstone3.InDTO.FranchiseOfferIn;
import com.example.capstone3.Model.Individual;
import com.example.capstone3.Service.FranchiseOfferService;
import com.example.capstone3.Service.IndividualService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deal-hub/individual")
@AllArgsConstructor
// Done by Salem
public class IndividualController {

    private final IndividualService individualService;
    private final FranchiseOfferService franchiseOfferService;

    @PostMapping("/add")
    public ResponseEntity addIndividual(@RequestBody @Valid Individual individual) {

        individualService.addIndividual(individual);
        return ResponseEntity.status(200).body("Individual added successfully");

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateIndividual(@PathVariable Integer id, @RequestBody @Valid Individual individual) {

        individualService.updateIndividual(id,individual);
        return ResponseEntity.status(200).body("Individual updated successfully");
    }



}
