package com.example.capstone3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rental {

    //by alaa

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // General Shop Information
    @NotEmpty(message = "Shop name is required.")
    private String name;

    @Pattern(regexp = "^(Available|Occupied)$",
            message = "Availability status must be 'Available', 'Occupied'")
    @NotEmpty(message = "Shop status is required. ")
    private String avaiilableStatus;

    @NotEmpty(message = "City is required.")
    private String city;

    @NotEmpty(message = "Location is required.")
    private String location;

    @Positive(message = "Area must be a positive number.")
    private Double area;


    // Rental Details

    @Column(columnDefinition = "varchar(8) default 'Yearly'")
    private String rentalType;

//    @Positive(message = "Rental duration must be positive number")
//    private Integer rentalDuration;

    @PositiveOrZero(message = "Annul rent must be zero or positive.")
    private Double annulRent;

//    @NotEmpty(message = "Lease term is required.")
//    private String leaseTerm; // e.g., "1 year", "6 months"


    // Unit Details (Unit Specific Information)
    @NotEmpty(message = "Unit type is required.")
    private String unitType; // e.g., "Apartment", "Shop", "Office"

    @NotEmpty(message = "Floor number is required.")
    private String floor; // e.g., "Ground", "1st Floor"

    @Positive(message = "Unit age must be a positive number.")
    private Integer unitAge; // Age of the unit in years

    @NotEmpty(message = "Unit number is required.")
    private String unitNumber; // Unit identification number

    @Positive(message = "Unit area must be a positive number.")
    private Double unitArea; // Area of the unit (in square meters)

    // Timestamps
    @Column(updatable = false)
    private LocalDate createdAt ;

    private LocalDate updatedAt ;




    // Relations
    @ManyToOne
    @NotNull(message = "Owner is required.")
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private Owner owner; // Linked to the Owner entity



    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Set<RentalOffer> rentalOffers; // Related rental requests


    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Set<RentalContract> rentalContracts;




}