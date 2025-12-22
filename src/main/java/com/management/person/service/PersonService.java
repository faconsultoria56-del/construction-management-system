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
import com.management.person.model.PersonDocument;
import com.management.person.model.UserAccount;
import com.management.person.repository.PersonDocumentRepository;
import com.management.person.repository.PersonRepository;
import com.management.person.repository.UserAccountRepository;
import com.management.plantype.model.PlanType;
import com.management.plantype.repository.PlanTypeRepository;
import com.management.project.exception.ResourceNotFoundException;
import com.management.role.model.PersonRole;
import com.management.role.model.Role;
import com.management.role.repository.PersonRoleRepository;
import com.management.role.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);


    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final BrasilApiService brasilApiService;
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final PersonDocumentRepository personDocumentRepository;
    private final UserAccountRepository userAccountRepository;
    private final PlanTypeRepository planTypeRepository;
    private final RoleRepository roleRepository;
    private final PersonRoleRepository personRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, CompanyRepository companyRepository, CompanyMemberRepository companyMemberRepository, BrasilApiService brasilApiService, AddressRepository addressRepository, CityRepository cityRepository, StateRepository stateRepository, DocumentTypeRepository documentTypeRepository, PersonDocumentRepository personDocumentRepository, UserAccountRepository userAccountRepository, PlanTypeRepository planTypeRepository, RoleRepository roleRepository, PersonRoleRepository personRoleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.companyMemberRepository = companyMemberRepository;
        this.brasilApiService = brasilApiService;
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.personDocumentRepository = personDocumentRepository;
        this.userAccountRepository = userAccountRepository;
        this.planTypeRepository = planTypeRepository;
        this.roleRepository = roleRepository;
        this.personRoleRepository = personRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PersonDTO createPerson(PersonCreateDTO createDTO) {
        // 1. Save Person
        Person person = modelMapper.map(createDTO, Person.class);
        Person savedPerson = personRepository.save(person);

        // 2. Save PersonDocument
        DocumentType documentType = documentTypeRepository.getReferenceById(createDTO.getDocumentType());
        PersonDocument personDocument = new PersonDocument();
        personDocument.setPerson(savedPerson);
        personDocument.setDocumentType(documentType);
        personDocument.setDocument(createDTO.getDocument());
        personDocumentRepository.save(personDocument);

        // 3. Create UserAccount
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(createDTO.getEmail());
        userAccount.setPasswordHash(passwordEncoder.encode(createDTO.getPassword()));
        userAccount.setPerson(savedPerson);
        userAccountRepository.save(userAccount);

        Role ownerRole = roleRepository.findByName("Owner")
                .orElseThrow(() -> new IllegalStateException("Owner role not found"));

        // 4. Resiliently handle Company creation
        if (createDTO.getCnpj() != null && !createDTO.getCnpj().isEmpty()) {
            try {
                CnpjResponseDTO cnpjData = brasilApiService.getCnpjData(createDTO.getCnpj()).block();

                if (cnpjData != null) {
                    // 1. Localizar dependências obrigatórias para a Company
                    DocumentType cnpjDocType = documentTypeRepository.findByCode("CNPJ")
                            .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento 'CNPJ' não encontrado no sistema."));

                    PlanType freePlan = planTypeRepository.findByCode("FREE")
                            .orElseThrow(() -> new ResourceNotFoundException("Plano 'FREE' não encontrado no sistema."));

                    // 2. Configurar e salvar a Company
                    Company company = new Company();
                    company.setDocument(createDTO.getCnpj());
                    company.setDocumentType(cnpjDocType); // Resolve o erro de Not-Null Constraint
                    company.setPlanType(freePlan);        // Resolve a obrigatoriedade de plano
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

                    Company savedCompany = companyRepository.save(company);

                    CompanyMember member = new CompanyMember();
                    member.setPerson(savedPerson);
                    member.setCompany(savedCompany);
                    member.setRole(ownerRole);
                    companyMemberRepository.save(member);
                }
            } catch (Exception e) {
                log.error("Failed to retrieve or process CNPJ data for {}: {}", createDTO.getCnpj(), e.getMessage());
                // Do not rethrow, allowing person creation to succeed
            }
        }

        // 5. Assign 'Owner' role
        PersonRole personRole = new PersonRole();
        personRole.setPerson(savedPerson);
        personRole.setRole(ownerRole);
        personRoleRepository.save(personRole);


        return modelMapper.map(savedPerson, PersonDTO.class);
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
                .map(companyMember -> modelMapper.map(companyMember.getPerson(), PersonDTO.class))
                .collect(Collectors.toList());
    }
}
