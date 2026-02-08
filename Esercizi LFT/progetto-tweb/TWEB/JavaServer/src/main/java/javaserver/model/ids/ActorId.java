package javaserver.model.ids;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for Actor entity.
 */
public class ActorId implements Serializable {
    private Long id; // movie_id
    private String name;
    private String role;

    public ActorId() {}

    public ActorId(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorId actorId = (ActorId) o;
        return Objects.equals(id, actorId.id) && 
               Objects.equals(name, actorId.name) && 
               Objects.equals(role, actorId.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, role);
    }
}
