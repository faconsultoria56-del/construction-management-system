package com.management.person.service;

import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.company.dto.CnpjResponseDTO;
import com.management.company.model.Company;
import com.management.company.repository.CompanyMemberRepository;
import com.management.company.repository.CompanyRepository;
import com.management.company.service.BrasilApiService;
import com.management.documenttype.model.DocumentType;
import com.management.documenttype.repository.DocumentTypeRepository;
import com.management.person.dto.PersonCreateDTO;
import com.management.person.dto.PersonDTO;
import com.management.person.model.Person;
import com.management.person.model.UserAccount;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private PersonDocumentRepository personDocumentRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
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
        createDTO.setDocumentType(1L);
        createDTO.setEmail("test@example.com");
        createDTO.setPassword("password");
        createDTO.setCnpj("12345678901234");

        person = new Person();
        person.setId(1L);

        ownerRole = new Role();
        ownerRole.setName("Owner");
    }

    @Test
    void createPerson_withValidCnpj_shouldCreateAllEntities() {
        // Arrange
        CnpjResponseDTO cnpjResponse = new CnpjResponseDTO();
        cnpjResponse.setRazaoSocial("Test Company");
        cnpjResponse.setLogradouro("Test Street");
        cnpjResponse.setCep("12345-678"); // Add CEP to trigger address creation

        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(documentTypeRepository.getReferenceById(anyLong())).thenReturn(new DocumentType());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(brasilApiService.getCnpjData(anyString())).thenReturn(Mono.just(cnpjResponse));
        when(roleRepository.findByName("Owner")).thenReturn(Optional.of(ownerRole));
        when(addressRepository.save(any(Address.class))).thenReturn(new Address());
        when(companyRepository.save(any(Company.class))).thenReturn(new Company());
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(new UserAccount());
        when(modelMapper.map(any(Person.class), eq(PersonDTO.class))).thenReturn(new PersonDTO());

        // Act
        PersonDTO result = personService.createPerson(createDTO);

        // Assert
        assertNotNull(result);
        verify(personRepository, times(1)).save(any());
        verify(personDocumentRepository, times(1)).save(any());
        verify(userAccountRepository, times(1)).save(any());
        verify(companyRepository, times(1)).save(any());
        verify(addressRepository, times(1)).save(any());
        verify(companyMemberRepository, times(1)).save(any());
        verify(personRoleRepository, times(1)).save(any());
    }

    @Test
    void createPerson_whenCnpjApiFails_shouldThrowException() {
        // Arrange
        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(documentTypeRepository.getReferenceById(anyLong())).thenReturn(new DocumentType());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(brasilApiService.getCnpjData(anyString())).thenReturn(Mono.error(new RuntimeException("API Error")));
        when(roleRepository.findByName("Owner")).thenReturn(Optional.of(ownerRole));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            personService.createPerson(createDTO);
        });

        verify(companyRepository, never()).save(any());
        verify(userAccountRepository, never()).save(any());
        verify(companyMemberRepository, never()).save(any());
    }

    @Test
    void createPerson_withoutCnpj_shouldThrowException() {
        // Arrange
        createDTO.setCnpj(null);

        when(modelMapper.map(any(PersonCreateDTO.class), eq(Person.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(documentTypeRepository.getReferenceById(anyLong())).thenReturn(new DocumentType());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(roleRepository.findByName("Owner")).thenReturn(Optional.of(ownerRole));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            personService.createPerson(createDTO);
        });

        verify(companyRepository, never()).save(any());
        verify(userAccountRepository, never()).save(any());
        verify(companyMemberRepository, never()).save(any());
    }
}
