package com.management.person.service;

import com.management.address.repository.AddressRepository;
import com.management.city.repository.CityRepository;
import com.management.city.repository.StateRepository;
import com.management.company.dto.CnpjResponseDTO;
import com.management.company.repository.CompanyMemberRepository;
import com.management.company.repository.CompanyRepository;
import com.management.company.service.BrasilApiService;
import com.management.documenttype.model.DocumentType;
import com.management.documenttype.repository.DocumentTypeRepository;
import com.management.person.dto.PersonCreateDTO;
import com.management.person.dto.PersonDTO;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.persondocument.repository.PersonDocumentRepository;
import com.management.role.model.Role;
import com.management.role.repository.RoleRepository;
import com.management.useraccount.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
    private BrasilApiService brasilApiService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private StateRepository stateRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private PersonDocumentRepository personDocumentRepository;
    @Mock
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CompanyMemberRepository companyMemberRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        // This setup is required for the tests to run correctly.
        // It's not strictly necessary to re-initialize the service,
        // but it can be useful for more complex scenarios.
    }

    private PersonCreateDTO createTestPersonDTO(String cnpj) {
        PersonCreateDTO createDTO = new PersonCreateDTO();
        createDTO.setFullName("Test User");
        createDTO.setDocument("12345678900");
        createDTO.setDocumentType("CPF");
        createDTO.setEmail("test@example.com");
        createDTO.setPassword("password");
        if (cnpj != null) {
            createDTO.setCnpj(cnpj);
        }
        return createDTO;
    }

    @Test
    void createPerson_withCnpj_shouldCreateCompanyAndPartner() {
        // Arrange
        PersonCreateDTO createDTO = createTestPersonDTO("12345678901234");
        Person person = new Person();
        person.setId(1);
        CnpjResponseDTO cnpjResponse = new CnpjResponseDTO();
        cnpjResponse.setRazaoSocial("Test Company");
        DocumentType docType = new DocumentType();
        docType.setCode("CPF");
        Role ownerRole = new Role();
        ownerRole.setName("Owner");

        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(brasilApiService.getCnpjData(anyString())).thenReturn(Mono.just(cnpjResponse));
        when(companyRepository.save(any(com.management.company.model.Company.class))).thenReturn(new com.management.company.model.Company());
        when(documentTypeRepository.findByCode("CPF")).thenReturn(Optional.of(docType));
        when(roleRepository.findByName("Owner")).thenReturn(Optional.of(ownerRole));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(modelMapper.map(any(Person.class), eq(PersonDTO.class))).thenReturn(new PersonDTO());

        // Act
        PersonDTO result = personService.createPerson(createDTO);

        // Assert
        assertNotNull(result);
        verify(companyRepository, times(1)).save(any());
        verify(companyMemberRepository, times(1)).save(any());
        verify(userAccountRepository, times(1)).save(any());
        verify(personDocumentRepository, times(1)).save(any());
    }

    @Test
    void createPerson_withoutCnpj_shouldNotCreateCompany() {
        // Arrange
        PersonCreateDTO createDTO = createTestPersonDTO(null);
        Person person = new Person();
        person.setId(1);
        DocumentType docType = new DocumentType();
        docType.setCode("CPF");

        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(documentTypeRepository.findByCode("CPF")).thenReturn(Optional.of(docType));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(modelMapper.map(any(Person.class), eq(PersonDTO.class))).thenReturn(new PersonDTO());

        // Act
        PersonDTO result = personService.createPerson(createDTO);

        // Assert
        assertNotNull(result);
        verify(companyRepository, never()).save(any());
        verify(companyMemberRepository, never()).save(any());
        verify(userAccountRepository, times(1)).save(any());
        verify(personDocumentRepository, times(1)).save(any());
    }
}
