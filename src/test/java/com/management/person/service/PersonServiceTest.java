package com.management.person.service;

import com.management.address.repository.AddressRepository;
import com.management.city.repository.CityRepository;
import com.management.city.repository.StateRepository;
import com.management.company.dto.CnpjResponseDTO;
import com.management.company.repository.CompanyPartnerRepository;
import com.management.company.repository.CompanyRepository;
import com.management.company.service.BrasilApiService;
import com.management.person.dto.PersonCreateDTO;
import com.management.person.dto.PersonDTO;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CompanyPartnerRepository companyPartnerRepository;
    @Mock
    private BrasilApiService brasilApiService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private StateRepository stateRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PersonService personService;

    @Test
    void createPerson_withCnpj_shouldCreateCompanyAndPartner() {
        // Arrange
        PersonCreateDTO createDTO = new PersonCreateDTO();
        createDTO.setFullName("Test User");
        createDTO.setCnpj("12345678901234");

        Person person = new Person();
        person.setId(1);

        CnpjResponseDTO cnpjResponse = new CnpjResponseDTO();
        cnpjResponse.setRazaoSocial("Test Company");

        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(brasilApiService.getCnpjData(anyString())).thenReturn(Mono.just(cnpjResponse));
        when(modelMapper.map(any(Person.class), eq(PersonDTO.class))).thenReturn(new PersonDTO());

        // Act
        PersonDTO result = personService.createPerson(createDTO);

        // Assert
        assertNotNull(result);
        verify(companyRepository, times(1)).save(any());
        verify(companyPartnerRepository, times(1)).save(any());
    }

    @Test
    void createPerson_withoutCnpj_shouldNotCreateCompany() {
        // Arrange
        PersonCreateDTO createDTO = new PersonCreateDTO();
        createDTO.setFullName("Test User");

        Person person = new Person();
        person.setId(1);

        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(modelMapper.map(any(Person.class), eq(PersonDTO.class))).thenReturn(new PersonDTO());

        // Act
        PersonDTO result = personService.createPerson(createDTO);

        // Assert
        assertNotNull(result);
        verify(companyRepository, never()).save(any());
        verify(companyPartnerRepository, never()).save(any());
    }
}
