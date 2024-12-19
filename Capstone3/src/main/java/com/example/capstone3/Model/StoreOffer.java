package com.example.capstone3.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @Column(nullable = false)
    private Double offerPrice;


    @Column(nullable = false)
    private Boolean sameDeal;
    @NotBlank(message = "Error:Status is required")
    @Pattern(regexp = "^(Pending by owner|Pending by individual|Approved|Rejected)$",
            message = "contract status must be 'Pending by owner', 'Pending by individual', 'Approved', 'Rejected'")
    @Column(nullable = false)
    private String status; // Pending by individual, Pending by owner, Accepted, Rejected


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "individual_id", nullable = false)
    private Individual individual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "storeOffer")
    private Set<StoreContract> storeContracts;

    @OneToMany(mappedBy = "storeOffer")
    private Set<Message> messages;
}
