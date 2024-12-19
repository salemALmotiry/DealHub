package com.example.capstone3.InDTO;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
// Done by Salem
public class FranchiseOfferIn {

    @NotNull(message = "Counter offer fee must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Counter offer fee must be greater than 0")
    private Double counterOfferFee;


    @Column(nullable = false)
    @NotNull(message = "Contract Duration is required")
    @Min(value = 1, message = "Contract Duration must be at least 1 year")
    @Max(value = 100, message = "Contract Duration must not exceed 100 years")
    private Integer counterContractDuration;

    @NotNull(message = "Counter investment amount must not be null")
    @DecimalMin(value = "1000", message = "Investment amount must be at least 1000")
    private Double counterInvestmentAmount;

    @NotNull(message = "Counter ongoing admin fees must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Admin fees cannot be negative")
    private Double counterOngoingAdminFees;

    @NotNull(message = "Message must not be null")
    private String message;




}
