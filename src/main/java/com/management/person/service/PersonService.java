package com.management.person.service;

import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.city.model.City;
import com.management.city.model.State;
import com.management.city.repository.CityRepository;
import com.management.city.repository.StateRepository;
import com.management.company.dto.CnpjResponseDTO;
import com.management.company.model.Company;
import com.management.company.model.CompanyPartner;
import com.management.company.repository.CompanyPartnerRepository;
import com.management.company.repository.CompanyRepository;
import com.management.company.service.BrasilApiService;
import com.management.person.dto.PersonCreateDTO;
import com.management.company.repository.CompanyPartnerRepository;
import com.management.person.dto.PersonDTO;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.project.exception.BusinessException;
import com.management.project.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final CompanyPartnerRepository companyPartnerRepository;
    private final BrasilApiService brasilApiService;
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, CompanyRepository companyRepository, CompanyPartnerRepository companyPartnerRepository, BrasilApiService brasilApiService, AddressRepository addressRepository, CityRepository cityRepository, StateRepository stateRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.companyPartnerRepository = companyPartnerRepository;
        this.brasilApiService = brasilApiService;
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PersonDTO createPerson(PersonCreateDTO createDTO) {
        Person person = modelMapper.map(createDTO, Person.class);
        Person savedPerson = personRepository.save(person);

        if (createDTO.getCnpj() != null && !createDTO.getCnpj().isEmpty()) {
            CnpjResponseDTO cnpjData = brasilApiService.getCnpjData(createDTO.getCnpj()).block();

            if (cnpjData != null) {
                Company company = new Company();
                company.setDocument(createDTO.getCnpj());
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

                CompanyPartner partner = new CompanyPartner();
                partner.setPerson(savedPerson);
                partner.setCompany(savedCompany);
                companyPartnerRepository.save(partner);
            }
        }
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
