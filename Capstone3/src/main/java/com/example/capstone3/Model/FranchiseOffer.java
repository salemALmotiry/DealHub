package com.example.capstone3.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

// Done by Salem

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class FranchiseOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "Offered Fee is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Offered Fee must be greater than 0")
    private Double offeredFee;

    @Column(nullable = true)
    @DecimalMin(value = "0.0", inclusive = false, message = "Counter Offer Fee must be greater than 0")
    private Double counterOfferFee;

    @Column(nullable = false)

    private Integer contractDuration;

    @Column(nullable = false)
    @NotNull(message = "Investment Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Investment Amount must be greater than 0")
    private Double investmentAmount;



    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Ongoing Admin Fees must be greater than 0")
    private Double ongoingAdminFees;

    // counter

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer counterContractDuration = 0;


    @Column(nullable = false)
    private Double counterInvestmentAmount = 0.00;


    @Column(nullable = false)
    private Double counterOngoingAdminFees = 0.0;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean sameDeal= true;


    // ------------------------------

    @Column(nullable = false)
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(Pending by owner|Pending by individual|Approved|Rejected)$",
            message = "contract status must be 'Pending by owner', 'Pending by individual', 'Approved', 'Rejected'")
    private String status; // Pending by individual, Pending by owner, Accepted, Rejected

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    // relations

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "individual_id", nullable = false)
    private Individual individual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id", nullable = false)
    private Franchise franchise;

    @OneToMany(mappedBy = "franchiseOffer", cascade = CascadeType.ALL)
    private Set<FranchiseContracts> franchiseContracts;


}