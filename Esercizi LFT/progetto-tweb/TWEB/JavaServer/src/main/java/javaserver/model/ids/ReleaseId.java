package javaserver.model.ids;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for Release entity.
 */
public class ReleaseId implements Serializable {
    private Long id; // movie_id
    private String country;
    private String date;
    private String type;

    public ReleaseId() {}

    public ReleaseId(Long id, String country, String date, String type) {
        this.id = id;
        this.country = country;
        this.date = date;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReleaseId releaseId = (ReleaseId) o;
        return Objects.equals(id, releaseId.id) && 
               Objects.equals(country, releaseId.country) && 
               Objects.equals(date, releaseId.date) && 
               Objects.equals(type, releaseId.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, date, type);
    }
}
