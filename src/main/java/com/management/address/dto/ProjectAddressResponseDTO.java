package com.management.address.dto;

import com.management.city.dto.CityResponseDTO;
import lombok.Data;

@Data
public class ProjectAddressResponseDTO {
    private Long id;
    private String street;
    private String number;
    private String neighborhood;
    private String zipCode;
    private String complement;
    private CityResponseDTO city;
}
