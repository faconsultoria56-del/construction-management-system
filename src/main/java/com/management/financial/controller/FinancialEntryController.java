package com.management.financial.controller;

import com.management.financial.dto.FinancialEntryRequestDTO;
import com.management.financial.dto.FinancialEntryResponseDTO;
import com.management.financial.service.FinancialEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/financial-entries")
public class FinancialEntryController {

    @Autowired
    private FinancialEntryService financialEntryService;

    @PostMapping
    public ResponseEntity<FinancialEntryResponseDTO> createFinancialEntry(@RequestBody FinancialEntryRequestDTO requestDTO) {
        FinancialEntryResponseDTO responseDTO = financialEntryService.createFinancialEntry(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FinancialEntryResponseDTO>> getAllFinancialEntries() {
        List<FinancialEntryResponseDTO> responseDTOs = financialEntryService.getAllFinancialEntries();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialEntryResponseDTO> getFinancialEntryById(@PathVariable Long id) {
        FinancialEntryResponseDTO responseDTO = financialEntryService.getFinancialEntryById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialEntryResponseDTO> updateFinancialEntry(@PathVariable Long id, @RequestBody FinancialEntryRequestDTO requestDTO) {
        FinancialEntryResponseDTO responseDTO = financialEntryService.updateFinancialEntry(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialEntry(@PathVariable Long id) {
        financialEntryService.deleteFinancialEntry(id);
        return ResponseEntity.noContent().build();
    }
}
