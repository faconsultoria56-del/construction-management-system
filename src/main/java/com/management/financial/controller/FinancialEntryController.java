package com.management.financial.controller;

import com.management.financial.dto.FinancialEntryDTO;
import com.management.financial.service.FinancialEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/financial-entries")
public class FinancialEntryController {

    @Autowired
    private FinancialEntryService financialEntryService;

    @PostMapping
    public FinancialEntryDTO createFinancialEntry(@RequestBody FinancialEntryDTO financialEntryDTO) {
        return financialEntryService.createFinancialEntry(financialEntryDTO);
    }

    @GetMapping
    public List<FinancialEntryDTO> getAllFinancialEntries() {
        return financialEntryService.getAllFinancialEntries();
    }

    @GetMapping("/{id}")
    public FinancialEntryDTO getFinancialEntryById(@PathVariable Integer id) {
        return financialEntryService.getFinancialEntryById(id);
    }

    @PutMapping("/{id}")
    public FinancialEntryDTO updateFinancialEntry(@PathVariable Integer id, @RequestBody FinancialEntryDTO financialEntryDTO) {
        return financialEntryService.updateFinancialEntry(id, financialEntryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialEntry(@PathVariable Integer id) {
        financialEntryService.deleteFinancialEntry(id);
        return ResponseEntity.noContent().build();
    }
}
