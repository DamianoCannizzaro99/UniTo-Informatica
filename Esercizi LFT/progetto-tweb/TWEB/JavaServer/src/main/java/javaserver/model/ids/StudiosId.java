package javaserver.model.ids;

import java.io.Serializable;

public class StudiosId implements Serializable {

    private Long id; // Foreign key referring to Movies.id

    private String studio; // Name of the studio
}
