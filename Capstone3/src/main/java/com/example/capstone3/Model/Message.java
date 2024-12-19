package com.example.capstone3.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Error: message is empty")
    @Column(nullable = false)
    private String message;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "individual_id")
    private Individual individual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_offer_id")
    private StoreOffer storeOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_offer_id")
    private FranchiseOffer franchiseOffer;
}
