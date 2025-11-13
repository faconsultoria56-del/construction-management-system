package com.management.city.service;

import com.management.city.model.City;
import com.management.city.repository.CityRepository;
import com.management.city.model.State;
import org.springframework.stereotype.Service;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City findOrCreate(String name, State state) {
        return cityRepository.findByNameAndState(name, state).orElseGet(() -> {
            City newCity = new City();
            newCity.setName(name);
            newCity.setState(state);
            return cityRepository.save(newCity);
        });
    }
}
