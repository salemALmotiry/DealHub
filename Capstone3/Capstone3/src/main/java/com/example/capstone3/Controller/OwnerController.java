package com.example.capstone3.Controller;

import com.example.capstone3.InDTO.FranchiseOfferIn;
import com.example.capstone3.Model.Individual;
import com.example.capstone3.Model.Owner;
import com.example.capstone3.Service.FranchiseOfferService;
import com.example.capstone3.Service.IndividualService;
import com.example.capstone3.Service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/owner")
public class OwnerController {

    private final OwnerService OwnerService;
    private final OwnerService ownerService;
    private final FranchiseOfferService franchiseOfferService;

    @PostMapping("/add")
    public ResponseEntity addIndividual(@RequestBody @Valid Owner owner) {

        OwnerService.add(owner);
        return ResponseEntity.status(200).body("Owner added successfully");

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateIndividual(@PathVariable Integer id, @RequestBody @Valid Owner owner) {

        OwnerService.update(id,owner);
        return ResponseEntity.status(200).body("Individual updated successfully");
    }












}
