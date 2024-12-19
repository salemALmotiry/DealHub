package com.example.capstone3.OutDTO;

import com.example.capstone3.Model.Owner;
import com.example.capstone3.Model.StoreOffer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StoreDTO {


    private Integer id;

    private String StoreName;


    private String location;


    private Boolean withEquipment;

    private String storeType;

    private Integer numberOfBranches;

    private Double originalPrice;


    private String description;


    private String ownerName;

    private String status;


}
