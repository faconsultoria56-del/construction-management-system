package com.management.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectAddressRequestDTO {

    @NotBlank(message = "Street is mandatory")
    private String street;

    @NotBlank(message = "Number is mandatory")
    private String number;

    @NotBlank(message = "Neighborhood is mandatory")
    private String neighborhood;

    @NotBlank(message = "Zip code is mandatory")
    private String zipCode;

    private String complement;

    @NotNull(message = "City ID is mandatory")
    private Integer cityId;
}
