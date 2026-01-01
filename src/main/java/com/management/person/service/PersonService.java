package com.management.person.service;

import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.city.model.City;
import com.management.city.model.State;
import com.management.city.repository.CityRepository;
import com.management.city.repository.StateRepository;
import com.management.company.dto.CnpjResponseDTO;
import com.management.company.model.Company;
import com.management.company.model.CompanyMember;
import com.management.company.repository.CompanyMemberRepository;
import com.management.company.repository.CompanyRepository;
import com.management.company.service.BrasilApiService;
import com.management.documenttype.model.DocumentType;
import com.management.documenttype.repository.DocumentTypeRepository;
import com.management.person.dto.PersonCreateDTO;
import com.management.person.dto.PersonDTO;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.persondocument.model.PersonDocument;
import com.management.persondocument.repository.PersonDocumentRepository;
import com.management.project.exception.BusinessException;
import com.management.project.exception.ResourceNotFoundException;
import com.management.role.model.Role;
import com.management.role.repository.RoleRepository;
import com.management.useraccount.model.UserAccount;
import com.management.useraccount.repository.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final BrasilApiService brasilApiService;
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;
    private final PersonDocumentRepository personDocumentRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final RoleRepository roleRepository;
    private final CompanyMemberRepository companyMemberRepository;


    public PersonService(PersonRepository personRepository, CompanyRepository companyRepository, BrasilApiService brasilApiService, AddressRepository addressRepository, CityRepository cityRepository, StateRepository stateRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserAccountRepository userAccountRepository, PersonDocumentRepository personDocumentRepository, DocumentTypeRepository documentTypeRepository, RoleRepository roleRepository, CompanyMemberRepository companyMemberRepository) {
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.brasilApiService = brasilApiService;
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userAccountRepository = userAccountRepository;
        this.personDocumentRepository = personDocumentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.roleRepository = roleRepository;
        this.companyMemberRepository = companyMemberRepository;
    }

    @Transactional
    public PersonDTO createPerson(PersonCreateDTO createDTO) {
        // Passo A: A Pessoa
        Person person = modelMapper.map(createDTO, Person.class);
        Person savedPerson = personRepository.save(person);

        // Passo B: O Documento
        DocumentType documentType = documentTypeRepository.findByCode(createDTO.getDocumentType())
                .orElseThrow(() -> new BusinessException("DocumentType not found for code: " + createDTO.getDocumentType()));
        PersonDocument personDocument = new PersonDocument(savedPerson, documentType, createDTO.getDocument());
        personDocumentRepository.save(personDocument);

        // Passo C: A Conta e Segurança
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(createDTO.getEmail());
        userAccount.setPasswordHash(passwordEncoder.encode(createDTO.getPassword()));
        userAccount.setPerson(savedPerson);
        userAccountRepository.save(userAccount);

        // Passo D: A Empresa e Passo E: O Papel (Role)
        if (createDTO.getCnpj() != null && !createDTO.getCnpj().isEmpty()) {
            Company savedCompany = createCompanyFromCnpj(createDTO.getCnpj());

            Role ownerRole = roleRepository.findByName("Owner")
                    .orElseThrow(() -> new BusinessException("Role 'Owner' not found."));

            CompanyMember companyMember = new CompanyMember();
            companyMember.setCompany(savedCompany);
            companyMember.setPerson(savedPerson);
            companyMember.setRole(ownerRole);
            companyMemberRepository.save(companyMember);
        }

        return modelMapper.map(savedPerson, PersonDTO.class);
    }

    private Company createCompanyFromCnpj(String cnpj) {
        CnpjResponseDTO cnpjData = brasilApiService.getCnpjData(cnpj).block();
        if (cnpjData == null) {
            throw new BusinessException("Failed to fetch CNPJ data for: " + cnpj);
        }

        Company company = new Company();
        company.setDocument(cnpj);
        company.setRegisteredName(cnpjData.getRazaoSocial());
        company.setTradeName(cnpjData.getNomeFantasia());
        company.setRegistrationStatusDescription(cnpjData.getDescricaoSituacaoCadastral());
        company.setPrimaryPhone(cnpjData.getDddTelefone1());

        if (cnpjData.getCep() != null) {
            Address address = new Address();
            address.setStreet(cnpjData.getLogradouro());
            address.setNumber(cnpjData.getNumero());
            address.setComplement(cnpjData.getComplemento());
            address.setNeighborhood(cnpjData.getBairro());
            address.setZipCode(cnpjData.getCep());

            State state = stateRepository.findByUf(cnpjData.getUf()).orElseGet(() -> {
                State newState = new State();
                newState.setUf(cnpjData.getUf());
                return stateRepository.save(newState);
            });

            City city = cityRepository.findByNameAndState(cnpjData.getMunicipio(), state).orElseGet(() -> {
                City newCity = new City();
                newCity.setName(cnpjData.getMunicipio());
                newCity.setState(state);
                return cityRepository.save(newCity);
            });
            address.setCity(city);
            Address savedAddress = addressRepository.save(address);
            company.setAddress(savedAddress);
        }
        return companyRepository.save(company);
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
        return companyMemberRepository.findByCompanyId(companyId)
                .stream()
                .map(CompanyMember::getPerson)
                .map(person -> modelMapper.map(person, PersonDTO.class))
                .collect(Collectors.toList());
    }
}
