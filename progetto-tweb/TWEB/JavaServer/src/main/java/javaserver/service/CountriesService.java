package javaserver.service;

import javaserver.model.dto.CountriesDTO;
import javaserver.repository.CountriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountriesService {
    private CountriesRepository countriesRepository;

    /**
     * Constructs a CountriesService with the specified CountriesRepository.
     *
     * @param countriesRepository the repository for countries
     */
    @Autowired
    public CountriesService(CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    /**
     * Retrieves countries by the specified country ID.
     *
     * @param id the ID of the country
     * @return a list of countries associated with the specified country ID
     */
    public List<CountriesDTO> getFindCountriesByCountryId(Long id) {
        return countriesRepository.findCountriesByCountryId(id);

    }
}
