package javaserver.model.ids;

import java.io.Serializable;

public class CrewId implements Serializable {

    private Long id;
    private String name;
    private String role;

    public CrewId(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrewId crewId = (CrewId) o;
        return id.equals(crewId.id) && name.equals(crewId.name) && role.equals(crewId.role);
    }
    @Override
    public int hashCode() {
        return 31 * id.hashCode() + name.hashCode() + role.hashCode();
    }
}
