package com.management.address.dto;

import lombok.Data;

@Data
public class ProjectAddressResponseDTO {
    private Integer id;
    private String street;
    private String number;
    private String neighborhood;
    private String zipCode;
    private String complement;
    private String cityName;
    private String stateName;
    private String countryName;
}
