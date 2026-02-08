package javaserver.model.entity;

import jakarta.persistence.*;
import javaserver.model.ids.CountriesId;

/**
 * Entity class representing a country.
 */
@Entity
@Table(name= "countries")
@IdClass(CountriesId.class)
public class Countries {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @Column(name= "country", nullable= false, columnDefinition= "TEXT")
    private String country;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private Movie movie;

    /**
     * Default constructor.
     */
    public Countries() {}

    /**
     * Constructs a Countries with the specified id and country.
     *
     * @param id the id of the country
     * @param country the name of the country
     */
    public Countries(Long id, String country) {
        this.id = id;
        this.country = country;
    }

    /**
     * Returns the id of the country.
     *
     * @return the id of the country
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the country.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the country.
     *
     * @return the name of the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the name of the country.
     *
     * @param country the name to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
}