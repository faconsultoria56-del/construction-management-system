package com.management.financial.service;

import com.management.exception.ResourceNotFoundException;
import com.management.financial.dto.FinancialEntryRequestDTO;
import com.management.financial.dto.FinancialEntryResponseDTO;
import com.management.financial.model.CostCenter;
import com.management.financial.model.FinancialEntry;
import com.management.financial.repository.CostCenterRepository;
import com.management.financial.repository.FinancialEntryRepository;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialEntryService {

    @Autowired
    private FinancialEntryRepository financialEntryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CostCenterRepository costCenterRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public FinancialEntryResponseDTO createFinancialEntry(FinancialEntryRequestDTO requestDTO) {
        FinancialEntry financialEntry = modelMapper.map(requestDTO, FinancialEntry.class);
        setRelatedEntities(financialEntry, requestDTO);
        FinancialEntry savedEntry = financialEntryRepository.save(financialEntry);
        return convertToResponseDTO(savedEntry);
    }

    @Transactional(readOnly = true)
    public List<FinancialEntryResponseDTO> getAllFinancialEntries() {
        return financialEntryRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FinancialEntryResponseDTO getFinancialEntryById(Long id) {
        FinancialEntry financialEntry = findFinancialEntryById(id);
        return convertToResponseDTO(financialEntry);
    }

    @Transactional
    public FinancialEntryResponseDTO updateFinancialEntry(Long id, FinancialEntryRequestDTO requestDTO) {
        FinancialEntry existingEntry = findFinancialEntryById(id);
        modelMapper.map(requestDTO, existingEntry);
        setRelatedEntities(existingEntry, requestDTO);
        FinancialEntry updatedEntry = financialEntryRepository.save(existingEntry);
        return convertToResponseDTO(updatedEntry);
    }

    @Transactional
    public void deleteFinancialEntry(Long id) {
        if (!financialEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Financial Entry not found with id: " + id);
        }
        financialEntryRepository.deleteById(id);
    }

    private FinancialEntry findFinancialEntryById(Long id) {
        return financialEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Entry not found with id: " + id));
    }

    private void setRelatedEntities(FinancialEntry financialEntry, FinancialEntryRequestDTO requestDTO) {
        Project project = projectRepository.findById(requestDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + requestDTO.getProjectId()));
        CostCenter costCenter = costCenterRepository.findById(requestDTO.getCostCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("Cost Center not found with id: " + requestDTO.getCostCenterId()));

        financialEntry.setProject(project);
        financialEntry.setCostCenter(costCenter);

        if (requestDTO.getPersonId() != null) {
            Person person = personRepository.findById(requestDTO.getPersonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + requestDTO.getPersonId()));
            financialEntry.setPerson(person);
        } else {
            financialEntry.setPerson(null);
        }
    }

    private FinancialEntryResponseDTO convertToResponseDTO(FinancialEntry financialEntry) {
        FinancialEntryResponseDTO responseDTO = modelMapper.map(financialEntry, FinancialEntryResponseDTO.class);

        if (financialEntry.getProject() != null) {
            responseDTO.setProjectId(financialEntry.getProject().getId());
            responseDTO.setProjectName(financialEntry.getProject().getName());
        }

        if (financialEntry.getCostCenter() != null) {
            responseDTO.setCostCenterId(financialEntry.getCostCenter().getId());
            responseDTO.setCostCenterName(financialEntry.getCostCenter().getName());
        }

        if (financialEntry.getPerson() != null) {
            responseDTO.setPersonId(financialEntry.getPerson().getId());
            responseDTO.setPersonName(financialEntry.getPerson().getFullName());
        }

        return responseDTO;
    }
}
