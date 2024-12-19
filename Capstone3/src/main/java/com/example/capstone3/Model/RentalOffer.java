package com.example.capstone3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalOffer {

    //by alaa

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotEmpty(message = "Status is required.")
    private String status = "Pending"; // Pending, Negotiation, Accepted, Rejected


    private Double rentalAmount;
    // Financial Details

    @Column(columnDefinition = "varchar(8) default 'Yearly'")
    private String rentalType;

    @PositiveOrZero(message = "Proposed rent must be zero or positive.")
    private Double individualProposedRent; // Rent proposed by the user


    @PositiveOrZero(message = "Proposed rent must be zero or positive.")
    private Double ownerProposedRent;

    private Boolean individualAcceptedOwnerProposed;
    private Boolean ownerAcceptedIndividualProposal;



    // Timestamps
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime negotiatedAt ;


    //  Rental Shop Details

    @ManyToOne
    @NotNull(message = "Shop is required.")
    @JoinColumn(name = "rental_id", nullable = false)
    @JsonIgnore
    private Rental rental;

    //contract

    @OneToMany(mappedBy = "rentalOffer")
    private Set<RentalContract> rentalContracts;

    // User Details
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "individual is required.")
    @JoinColumn(name = "individual_id", nullable = false)
    @JsonIgnore
    private Individual individual; // Linked to the User entity



}
