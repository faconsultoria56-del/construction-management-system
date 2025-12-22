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
import com.management.plantype.model.PlanType;
import com.management.plantype.repository.PlanTypeRepository;
import com.management.person.model.Person;
import com.management.person.repository.PersonDocumentRepository;
import com.management.person.repository.PersonRepository;
import com.management.person.repository.UserAccountRepository;
import com.management.role.model.Role;
import com.management.role.repository.PersonRoleRepository;
import com.management.role.repository.RoleRepository;
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
    private CompanyMemberRepository companyMemberRepository;
    @Mock
    private BrasilApiService brasilApiService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private StateRepository stateRepository;
    @Mock
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private PersonDocumentRepository personDocumentRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private PlanTypeRepository planTypeRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PersonRoleRepository personRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PersonService personService;

    private PersonCreateDTO createDTO;
    private Person person;
    private Role ownerRole;

    @BeforeEach
    void setUp() {
        createDTO = new PersonCreateDTO();
        createDTO.setFullName("Test User");
        createDTO.setDocument("12345678900");
        createDTO.setDocumentType(1);
        createDTO.setEmail("test@example.com");
        createDTO.setPassword("password");
        createDTO.setCnpj("12345678901234");

        person = new Person();
        person.setId(1);

        ownerRole = new Role();
        ownerRole.setName("Owner");
    }

    @Test
    void createPerson_withValidCnpj_shouldCreateAllEntities() {
        // Arrange
        CnpjResponseDTO cnpjResponse = new CnpjResponseDTO();
        cnpjResponse.setRazaoSocial("Test Company");

        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(documentTypeRepository.getReferenceById(anyInt())).thenReturn(new DocumentType());
        when(documentTypeRepository.findByCode("CNPJ")).thenReturn(Optional.of(new DocumentType()));
        when(planTypeRepository.findByCode("FREE")).thenReturn(Optional.of(new PlanType()));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(brasilApiService.getCnpjData(anyString())).thenReturn(Mono.just(cnpjResponse));
        when(roleRepository.findByName("Owner")).thenReturn(Optional.of(ownerRole));
        when(modelMapper.map(any(Person.class), eq(PersonDTO.class))).thenReturn(new PersonDTO());

        // Act
        PersonDTO result = personService.createPerson(createDTO);

        // Assert
        assertNotNull(result);
        verify(personRepository, times(1)).save(any());
        verify(personDocumentRepository, times(1)).save(any());
        verify(userAccountRepository, times(1)).save(any());
        verify(companyRepository, times(1)).save(any());
        verify(companyMemberRepository, times(1)).save(any());
        verify(personRoleRepository, times(1)).save(any());
    }

    @Test
    void createPerson_withInvalidCnpj_shouldStillCreatePerson() {
        // Arrange
        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(documentTypeRepository.getReferenceById(anyInt())).thenReturn(new DocumentType());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(brasilApiService.getCnpjData(anyString())).thenReturn(Mono.error(new RuntimeException("API Error")));
        when(roleRepository.findByName("Owner")).thenReturn(Optional.of(ownerRole));
        when(modelMapper.map(any(Person.class), eq(PersonDTO.class))).thenReturn(new PersonDTO());

        // Act
        PersonDTO result = personService.createPerson(createDTO);

        // Assert
        assertNotNull(result);
        verify(personRepository, times(1)).save(any());
        verify(personDocumentRepository, times(1)).save(any());
        verify(userAccountRepository, times(1)).save(any());
        verify(companyRepository, never()).save(any());
        verify(companyMemberRepository, never()).save(any());
        verify(personRoleRepository, times(1)).save(any());
    }
}
