package com.example.capstone3.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
@Setter
public class StoreContractDTO {



    private String storeName;


    private String location;


    private Boolean withEquipment;

    private String storeType;

    private Integer numberOfBranches;


    private String description;


    private Double agreedPrice;




    private LocalDate contractDate;


    private String individualName;


    private String ownerName;

}
