package com.example.capstone3.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class StoreContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Error:Store Name is required")
    @Size(min = 3, max = 100 ,message = "Error: Store name length must between 3 to 100")
    @Column(nullable = false ,length = 100)
    private String storeName;

    @NotEmpty(message = "Error:location  is required")
    @Size(min = 3, max = 100 ,message = "Error: Store location length must between 3 to 100")
    @Column(nullable = false , length = 100)
    private String location;

    @NotNull(message = "Error:With Equipment is required")
    @Column(nullable = false)
    private Boolean withEquipment;

    @NotNull(message = "Error:store Type is required")
    @Column(nullable = false)
    private String storeType;

    @NotNull(message = "Error:number Of Branches is required")
    @Min(value = 1 , message = "Error:Area must be greater than 1")
    @Column(nullable = false)
    private Integer numberOfBranches;

    @NotEmpty(message = "Error:description is required")
    @Size(min = 3, max = 250 ,message = "Error: description length must between 3 to 300")
    @Column(nullable = false ,length = 250)
    private String description;

    @NotNull(message = "Error:Price is required")
    @Positive(message = "Error:Price must be greater than 0")
    @Column(nullable = false)
    private Double agreedPrice;


    @NotNull(message = "Error:Contract Date is required")
    @PastOrPresent(message = "Error: contractDate must be in present or past")
    @Column(nullable = false)
    private LocalDate contractDate;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now() ;
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now() ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_offer_id", nullable = false)
    private StoreOffer storeOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "individual_id", nullable = false)
    private Individual individual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
