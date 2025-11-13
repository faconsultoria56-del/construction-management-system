package com.management.city.service;

import com.management.city.model.State;
import com.management.city.repository.StateRepository;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public State findOrCreate(String uf) {
        return stateRepository.findByUf(uf).orElseGet(() -> {
            State newState = new State();
            newState.setUf(uf);
            return stateRepository.save(newState);
        });
    }
}
