package com.example.capstone3.InDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class StoreDTOIn {




    @NotEmpty(message = "Error:Store Name is required")
    @Size(min = 3, max = 100 ,message = "Error: Store name length must between 3 to 100")
    private String storeName;

    @NotEmpty(message = "Error:location  is required")
    @Size(min = 3, max = 100 ,message = "Error: Store location length must between 3 to 100")
    private String location;

    @NotNull(message = "Error:with Equipment is required")
    private Boolean withEquipment;

    @NotNull(message = "Error:store Type is required")
    private String storeType;

    @NotNull(message = "Error:number Of Branches is required")
    @Min(value = 1 , message = "Error:Area must be greater than 1")
    private Integer numberOfBranches;

    @NotNull(message = "Error:Price is required")
    @Positive(message = "Error:Price must be greater than 0")
    private Double originalPrice;

    @NotEmpty(message = "Error:description is required")
    @Size(min = 3, max = 250 ,message = "Error: description length must between 3 to 300")
    private String description;

    private Integer owner_id;
}
