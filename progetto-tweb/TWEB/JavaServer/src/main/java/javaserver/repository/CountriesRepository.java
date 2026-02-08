package javaserver.repository;

import javaserver.model.dto.CountriesDTO;
import javaserver.model.entity.Countries;
import javaserver.model.ids.CountriesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, CountriesId> {


    /**
     * Finds countries by the specified country ID.
     *
     * @param id the ID of the country
     * @return a list of countries associated with the specified country ID
     */
    @Query("SELECT new javaserver.model.dto.CountriesDTO(c.id, c.country) FROM Countries c WHERE c.id = :id")
    List<CountriesDTO> findCountriesByCountryId(@Param("id") Long id);
}
