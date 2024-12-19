package com.example.capstone3.Controller;

import com.example.capstone3.ApiResponse.ApiResponse;
import com.example.capstone3.Model.Franchise;
import com.example.capstone3.Repository.FranchiseRepository;
import com.example.capstone3.Service.FranchiseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/franchise")
// Done by Salem
public class FranchiseController {

    private final FranchiseService franchiseService;

    @PostMapping("/add")
    public ResponseEntity addFranchise(@RequestBody @Valid Franchise franchise) {
        franchiseService.add( franchise);
        return ResponseEntity.status(200).body(new ApiResponse("Franchise added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateFranchise(@PathVariable Integer id, @RequestBody @Valid Franchise franchise) {
        franchiseService.update(id,franchise);
        return ResponseEntity.status(200).body(new ApiResponse("Franchise updated successfully"));
    }

    @GetMapping("/get-franchise")
    public ResponseEntity getFranchise(){

        return ResponseEntity.status(200).body(franchiseService.franchises());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id){

        franchiseService.delete(id);
        return ResponseEntity.status(200).body(new ApiResponse("Franchise deleted successfully"));
    }
}
