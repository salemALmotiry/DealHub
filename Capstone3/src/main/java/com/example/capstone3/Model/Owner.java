package com.example.capstone3.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Full Name is required")
    @Size(max = 100, message = "Full Name must not exceed 100 characters")
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^\\+9665[0-9]{8}$", message = "Phone number must be a valid Saudi number starting with +9665 and followed by 8 digits")
    @Column(columnDefinition = "VARCHAR(60) NOT NULL UNIQUE")
    private String phoneNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL)
    private Set<Store> stores = new HashSet<>();
    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL)
    private Set<Rental> rentals = new HashSet<>();
    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL)
    private Set<Franchise> franchises = new HashSet<>();


    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL)
    private Set<StoreContract> storeContracts = new HashSet<>();
    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL)
    private Set<FranchiseContracts> franchiseContracts = new HashSet<>();
    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL)
    private Set<RentalContract> rentalContracts = new HashSet<>();



    // message
    @OneToMany(mappedBy = "owner")
    private Set<Message> messages;
}
