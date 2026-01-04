package com.management.address.service;

import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.company.dto.CnpjResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address createFromCnpj(CnpjResponseDTO dto) {
        Address address = new Address();
        address.setStreet(dto.getLogradouro());
        address.setNumber(dto.getNumero());
        address.setNeighborhood(dto.getBairro());
        address.setZipCode(dto.getCep());
        address.setComplement(dto.getComplemento());
        address.setCityName(dto.getMunicipio());
        address.setStateName(dto.getUf());
        return addressRepository.save(address);
    }
}
