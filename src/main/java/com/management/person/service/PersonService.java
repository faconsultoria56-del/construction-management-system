package com.management.person.service;

import com.management.person.dto.PersonCreateDTO;
import com.management.company.repository.CompanyPartnerRepository;
import com.management.person.dto.PersonDTO;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.project.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final CompanyPartnerRepository companyPartnerRepository;
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, CompanyPartnerRepository companyPartnerRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.companyPartnerRepository = companyPartnerRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PersonDTO createPerson(PersonCreateDTO createDTO) {
        Person person = modelMapper.map(createDTO, Person.class);
        person = personRepository.save(person);
        return modelMapper.map(person, PersonDTO.class);
    }

    public List<PersonDTO> findAll() {
        return personRepository.findAll()
                .stream()
                .map(person -> modelMapper.map(person, PersonDTO.class))
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Integer id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com o id: " + id));
        return modelMapper.map(person, PersonDTO.class);
    }

    public List<PersonDTO> findPersonsByCompanyId(Integer companyId) {
        return companyPartnerRepository.findByCompanyId(companyId)
                .stream()
                .map(companyPartner -> modelMapper.map(companyPartner.getPerson(), PersonDTO.class))
                .collect(Collectors.toList());
    }
}
