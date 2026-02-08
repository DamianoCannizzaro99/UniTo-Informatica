package javaserver.model.ids;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for Posters entity.
 */
public class PostersId implements Serializable {
    private Long id;
    private String link;

    public PostersId() {}

    public PostersId(Long id, String link) {
        this.id = id;
        this.link = link;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostersId postersId = (PostersId) o;
        return Objects.equals(id, postersId.id) && Objects.equals(link, postersId.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }
}
