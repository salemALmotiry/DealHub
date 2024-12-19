package com.example.capstone3.InDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreCounterOfferDTOIn {



    @NotNull(message = "Error:counter Price is required")
    @Positive(message = "Error:counter Price must be greater than 0")
    private Double counterPrice;

    @NotEmpty(message = "Error:message is required")
    @Size( min = 10, message = "Error: message length must more then 10")
    private String message;

}
