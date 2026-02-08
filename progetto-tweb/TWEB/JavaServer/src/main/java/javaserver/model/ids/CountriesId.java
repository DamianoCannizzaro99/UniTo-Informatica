package javaserver.model.ids;
import java.io.Serializable;
import java.util.Objects;

public class CountriesId implements Serializable{

    private Long id;
    private String country;

    public CountriesId() {
    }

    public CountriesId(Long id, String country) {
        this.id = id;
        this.country = country;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountriesId that = (CountriesId) o;
        return id.equals(that.id) && country.equals(that.country);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, country);
    }

}
