package com.example.capstone3.OutDTO;

import com.example.capstone3.Model.Owner;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
public class RentalDTO {

    // General Shop Information

    private Integer id;

    private String ownerName;

    private String rentalName;

    private Double annulRent;

    private String avaiilableStatus;

    private String city;

    private String location;

    @Column(columnDefinition = "varchar(8) default 'Yearly'")
    private String rentalType ;

    private String unitType; // e.g., "Apartment", "Shop", "Office"

    private String floor;

    private Integer unitAge; // Age of the unit in years

    private String unitNumber; // Unit identification number

    private Double unitArea; // Area of the unit (in square meters)


}
