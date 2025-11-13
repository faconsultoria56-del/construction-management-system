package com.management.state.service;

import com.management.state.model.State;
import com.management.state.repository.StateRepository;
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
