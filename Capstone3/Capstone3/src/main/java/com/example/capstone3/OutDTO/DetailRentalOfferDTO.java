package com.example.capstone3.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class DetailRentalOfferDTO {

    private Integer id;

    private String rentalName;

    private Double rentalAmount;

    private String status; // Pending, Negotiation, Accepted, Rejected

    private String individualName;
    private Boolean individualAcceptedOwnerProposed;
    private Double individualProposedRent; // Rent proposed by the user

    private String ownerName;
    private Boolean ownerAcceptedIndividualProposal;
    private Double ownerProposedRent; //response

    private LocalDateTime createdAt;
    private LocalDateTime negotiatedAt ;

}
