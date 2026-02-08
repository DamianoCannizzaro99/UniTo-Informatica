package javaserver.controller;


import javaserver.model.dto.CountriesDTO;
import javaserver.service.CountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountriesController {
    private final CountriesService countriesService;

    /**
     * Constructs a CountriesController with the specified CountriesService.
     *
     * @param countriesService the service for managing countries
     */
    @Autowired
    public CountriesController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<List<CountriesDTO>> getCountriesById(@PathVariable Long id) {
        List<CountriesDTO> countries = countriesService.getFindCountriesByCountryId(id);

        if (countries == null|| countries.isEmpty()) {
            // Returns 404 (Not Found) if no countries are found
            return ResponseEntity.notFound().build();
        }
        // Returns 200 (OK) with the country details
        return ResponseEntity.ok(countries);
    }
}
