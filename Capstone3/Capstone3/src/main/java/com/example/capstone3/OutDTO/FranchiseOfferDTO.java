package com.example.capstone3.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
// Done by Salem

public class FranchiseOfferDTO {


    private String franchiseName;

    private String offeredBy;

    private Double offeredFee;

    private Double counterOfferFee;

    private Integer contractDuration;

    private Integer counterContractDuration;

    private Double investmentAmount;

    private Double counterInvestmentAmount;


    private Double ongoingAdminFees;

    private Double counterOngoingAdminFees;


    private boolean sameDeal;

    // ------------------------------
    private String status; // Pending by owner,pending by Individual , Accepted, Rejected


}
