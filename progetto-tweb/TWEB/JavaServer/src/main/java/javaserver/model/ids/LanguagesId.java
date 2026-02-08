package javaserver.model.ids;

import java.io.Serializable;

/**
 * Composite key class for Languages.
 */
public class LanguagesId implements Serializable {
    private Long id;
    private String language;
    private String type;
}