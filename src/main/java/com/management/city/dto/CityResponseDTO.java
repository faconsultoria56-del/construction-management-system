package com.management.city.dto;

import com.management.state.dto.StateResponseDTO;
import lombok.Data;

@Data
public class CityResponseDTO {
    private Integer id;
    private String name;
    private StateResponseDTO state;
}
