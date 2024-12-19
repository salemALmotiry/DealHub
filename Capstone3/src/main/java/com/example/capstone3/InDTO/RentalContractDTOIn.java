package com.example.capstone3.InDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class RentalContractDTOIn {

    //by alaa

    @NotEmpty(message = "Contract number is required.")
    private String contractNumber;

    @Pattern(regexp = "^(Expired|Active)$",
            message = "Contract status must be 'Expired' or 'Active'")
    private String contractStatus;

    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;

    @NotNull(message = "Sealing date is required.")
    private LocalDate sealingDate;

    @NotEmpty(message = "Number of electricity bill is required.")
    private String nubmerElectricityBill;

    @NotEmpty(message = "Number of Water bill is required.")
    private String numberWaterBill;

    @NotNull(message = "Owner ID is required.")
    private Integer ownerId;

    @NotNull(message = "Individual ID is required.")
    private Integer individualId;

    @NotNull(message = "Rental Offer ID is required.")
    private Integer rentalOfferId;

    @NotNull(message = "Rental ID is required.")
    private Integer rentalId;

    @Column(updatable = false)
    private LocalDateTime createdAt;


}
