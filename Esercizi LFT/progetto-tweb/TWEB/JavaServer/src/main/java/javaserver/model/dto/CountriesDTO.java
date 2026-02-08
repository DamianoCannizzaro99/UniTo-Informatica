package javaserver.model.dto;

public class CountriesDTO {

    private Long id;
    private String country;

    public CountriesDTO() {
    }

    /**
     * Constructor to initialize CountriesDTO with id and country.
     *
     * @param id the unique identifier for the country
     * @param country the name of the country
     */
    public CountriesDTO(Long id, String country) {
        this.id = id;
        this.country = country;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
}
