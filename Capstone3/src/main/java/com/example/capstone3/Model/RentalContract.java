package com.example.capstone3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalContract {

    //by alaa

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    // بيانات العقد
    // General Contract Information
    @NotEmpty(message = "Contract number is required.")
    private String contractNumber;

    //@NotEmpty(message = "Contract status is required.")
    @Column(nullable = false)
    @Pattern(regexp = "^(Expired|Active)$",
            message = "Contract status must be 'Express' or 'Active' ")
    private String contractStatus;


    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;

    @NotNull(message = "Sealing date is required.")
    private LocalDate sealingDate;



    // Financial Information

    @Positive(message = "Total contract amount must be positive.")
    private Double totalContractAmount;


    @NotEmpty(message = "Number of electricity bill is required.")
    private String nubmerElectricityBill;

    @NotEmpty(message = "Number of Water bill is required.")
    private String numberWaterBill;


    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    //relations

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Owner is required.")
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private Owner owner; // المالك كطرف أول

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "individual is required.")
    @JoinColumn(name = "individual_id", nullable = false)
    @JsonIgnore
    private Individual individual; // المستخدم كطرف ثاني


    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Rental offer is required.")
    @JoinColumn(name = "rental_offer_id", nullable = false)
    @JsonIgnore
    private RentalOffer rentalOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id", nullable = false)
    @NotNull(message = "Rental is required.")
    private Rental rental;
}
