package com.bankingsystem.bankapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    @NotBlank
    private String accountHolderName;

    @Min(value = 0, message = "Initial deposit must be ≥ 0")
    private double initialDeposit;
}
