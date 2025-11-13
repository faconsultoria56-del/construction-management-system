package com.management.address.service;

import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.city.model.City;
import com.management.city.service.CityService;
import com.management.company.dto.CnpjResponseDTO;
import com.management.state.model.State;
import com.management.state.service.StateService;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CityService cityService;
    private final StateService stateService;

    public AddressService(AddressRepository addressRepository, CityService cityService, StateService stateService) {
        this.addressRepository = addressRepository;
        this.cityService = cityService;
        this.stateService = stateService;
    }

    public Address createFromCnpj(CnpjResponseDTO dto) {
        State state = stateService.findOrCreate(dto.getUf());
        City city = cityService.findOrCreate(dto.getMunicipio(), state);

        Address address = new Address();
        address.setStreet(dto.getLogradouro());
        address.setNumber(dto.getNumero());
        address.setNeighborhood(dto.getBairro());
        address.setZipCode(dto.getCep());
        address.setComplement(dto.getComplemento());
        address.setCity(city);
        return addressRepository.save(address);
    }
}
