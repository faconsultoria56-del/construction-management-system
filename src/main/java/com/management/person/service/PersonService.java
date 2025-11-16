package com.management.person.service;

import com.management.person.dto.PersonCreateDTO;
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
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
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
}
