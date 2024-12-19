package com.example.capstone3.InDTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@AllArgsConstructor
@Setter
@Getter
public class RentalDTOIn {

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


    @Column(columnDefinition = "varchar(8) default 'Yearly'")
    private String rentalType;

    @PositiveOrZero(message = "Annul rent must be zero or positive.")
    private Double annulRent;

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
    private LocalDate createdAt;


}
