package com.example.capstone3.Controller;

import com.example.capstone3.ApiResponse.ApiResponse;
import com.example.capstone3.InDTO.StoreDTOIn;
import com.example.capstone3.OutDTO.StoreDTO;
import com.example.capstone3.Service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deal-hub/store")

//Done by Basil
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/get")
    public ResponseEntity<List<StoreDTO>> getAllStore() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/get/{storeId}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Integer storeId) {
        return ResponseEntity.ok(storeService.getStoreById(storeId));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addStore(@RequestBody @Valid StoreDTOIn storeDTOIn) {
        storeService.addStore(storeDTOIn);
        return ResponseEntity.ok(new ApiResponse("Store added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateStore(@PathVariable Integer id, @RequestBody @Valid StoreDTOIn storeDTOIn) {
        storeService.updateStore(id,storeDTOIn);
        return ResponseEntity.ok(new ApiResponse("Store updated successfully"));
    }

    // active the store to allow the individual make offer on store and prevent the owner make changes to store -- by basil
    @PutMapping("/activate/{id}")
    public ResponseEntity<ApiResponse> activateStore(@PathVariable Integer id) {
        storeService.activeStore(id);
        return ResponseEntity.ok(new ApiResponse("Store activated successfully"));
    }
    // inactive the store reject all active offers and prevent the individual to make offer and allow the owner make changes to store -- by basil
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateStore(@PathVariable Integer id) {
        storeService.inactiveStore(id);
        return ResponseEntity.ok(new ApiResponse("Store deactivated successfully"));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteStore(@PathVariable Integer id){
        storeService.deleteStore(id);
        return ResponseEntity.ok(new ApiResponse("Store deleted successfully"));
    }


}
