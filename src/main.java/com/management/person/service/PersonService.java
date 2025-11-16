package com.management.person.service;

import com.management.address.model.Address;
import com.management.address.service.AddressService;
import com.management.company.dto.CnpjResponseDTO;
import com.management.company.dto.QsaResponseDTO;
import com.management.company.model.Company;
import com.management.company.model.CompanyPartner;
import com.management.company.repository.CompanyPartnerRepository;
import com.management.company.repository.CompanyRepository;
import com.management.company.service.BrasilApiService;
import com.management.person.dto.PersonCreateDTO;
import com.management.person.dto.PersonDTO;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final CompanyPartnerRepository companyPartnerRepository;
    private final AddressService addressService;
    private final BrasilApiService brasilApiService;
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, CompanyRepository companyRepository, CompanyPartnerRepository companyPartnerRepository, AddressService addressService, BrasilApiService brasilApiService, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.companyPartnerRepository = companyPartnerRepository;
        this.addressService = addressService;
        this.brasilApiService = brasilApiService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PersonDTO createPerson(PersonCreateDTO createDTO) {
        if (createDTO.getCnpj() != null && !createDTO.getCnpj().isEmpty()) {
            CnpjResponseDTO cnpjData = brasilApiService.getCnpjData(createDTO.getCnpj()).block();
            if (cnpjData != null) {
                return createPersonWithCompany(createDTO, cnpjData);
            }
        }
        Person person = modelMapper.map(createDTO, Person.class);
        person = personRepository.save(person);
        return modelMapper.map(person, PersonDTO.class);
    }

    private PersonDTO createPersonWithCompany(PersonCreateDTO createDTO, CnpjResponseDTO cnpjData) {
        Address address = addressService.createFromCnpj(cnpjData);

        Company company = new Company();
        company.setRegisteredName(cnpjData.getRazaoSocial());
        company.setTradeName(cnpjData.getNomeFantasia());
        company.setRegistrationStatusDescription(cnpjData.getDescricaoSituacaoCadastral());
        company.setPrimaryPhone(cnpjData.getDddTelefone1());
        company.setAddress(address);
        company = companyRepository.save(company);

        Person person = modelMapper.map(createDTO, Person.class);
        person.setCompany(company);
        person = personRepository.save(person);

        if (cnpjData.getQsa() != null) {
            for (QsaResponseDTO qsa : cnpjData.getQsa()) {
                Person partner = new Person();
                partner.setFullName(qsa.getNomeSocio());
                partner.setDocument(qsa.getCpfRepresentanteLegal());
                partner.setCompany(company);
                partner = personRepository.save(partner);

                CompanyPartner companyPartner = new CompanyPartner();
                companyPartner.setCompany(company);
                companyPartner.setPerson(partner);
                companyPartnerRepository.save(companyPartner);
            }
        }

        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);
        personDTO.setCompanyId(company.getId());
        return personDTO;
    }
}
