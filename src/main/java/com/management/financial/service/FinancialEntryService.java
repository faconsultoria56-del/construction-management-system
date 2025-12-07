package com.management.financial.service;

import com.management.financial.dto.FinancialEntryDTO;
import com.management.financial.model.FinancialEntry;
import com.management.financial.repository.FinancialEntryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialEntryService {

    @Autowired
    private FinancialEntryRepository financialEntryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public FinancialEntryDTO createFinancialEntry(FinancialEntryDTO financialEntryDTO) {
        FinancialEntry financialEntry = modelMapper.map(financialEntryDTO, FinancialEntry.class);
        financialEntry = financialEntryRepository.save(financialEntry);
        return modelMapper.map(financialEntry, FinancialEntryDTO.class);
    }

    public List<FinancialEntryDTO> getAllFinancialEntries() {
        return financialEntryRepository.findAll().stream()
                .map(financialEntry -> modelMapper.map(financialEntry, FinancialEntryDTO.class))
                .collect(Collectors.toList());
    }

    public FinancialEntryDTO getFinancialEntryById(Integer id) {
        FinancialEntry financialEntry = financialEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial Entry not found"));
        return modelMapper.map(financialEntry, FinancialEntryDTO.class);
    }

    public FinancialEntryDTO updateFinancialEntry(Integer id, FinancialEntryDTO financialEntryDTO) {
        FinancialEntry existingFinancialEntry = financialEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial Entry not found"));
        modelMapper.map(financialEntryDTO, existingFinancialEntry);
        existingFinancialEntry = financialEntryRepository.save(existingFinancialEntry);
        return modelMapper.map(existingFinancialEntry, FinancialEntryDTO.class);
    }

    public void deleteFinancialEntry(Integer id) {
        financialEntryRepository.deleteById(id);
    }
}
