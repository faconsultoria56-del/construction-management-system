package com.management.address.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "City name is mandatory")
    private String cityName;

    @NotBlank(message = "State name is mandatory")
    private String stateName;

    @NotBlank(message = "Country name is mandatory")
    private String countryName;
}
