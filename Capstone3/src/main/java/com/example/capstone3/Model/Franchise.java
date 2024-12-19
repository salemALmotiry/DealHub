package com.example.capstone3.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

// Done by Salem
public class Franchise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100,unique = true)
    @NotBlank(message = "Brand Name is required")
    @Size(max = 100, message = "Brand Name must not exceed 100 characters")
    private String brandName;

    @Column(length = 50)
    @NotBlank(message = "License Number is required")
    @Size(max = 50, message = "License Number must not exceed 50 characters")
    private String licenseNumber;

    @Column(nullable = false)
    @NotNull(message = "Contract Duration is required")
    @Min(value = 1, message = "Contract Duration must be at least 1 year")
    @Max(value = 100, message = "Contract Duration must not exceed 100 years")
    private Integer contractDuration;

    @Column(nullable = false)
    @NotNull(message = "Investment Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Investment Amount must be greater than 0")
    private Double investmentAmount;

    @Column(nullable = false)
    @NotNull(message = "Franchise Fee is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Franchise Fee must be greater than 0")
    private Double franchiseFee;

    @Column(nullable = false)
    @NotNull(message = "Number of Branches is required")
    private Integer numberOfBranches;

    @Column(length = 100)
    @NotBlank(message = "Country of Origin is required")
    @Size(max = 100, message = "Country of Origin must not exceed 100 characters")
    private String countryOfOrigin;

    @Column(length = 50)
    @NotBlank(message = "Franchise Type is required")
    private String franchiseType;

    @Column(length = 100)
    @NotBlank(message = "Sector is required")
    @Size(max = 100, message = "Sector must not exceed 100 characters")
    private String sector;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Ongoing Admin Fees must be greater than 0")
    private Double ongoingAdminFees;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String supportDetails;


    // relations

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @OneToMany(mappedBy = "franchise",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FranchiseOffer> franchiseOffer;


    @OneToMany(mappedBy = "franchise",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FranchiseContracts> franchiseContracts;
}