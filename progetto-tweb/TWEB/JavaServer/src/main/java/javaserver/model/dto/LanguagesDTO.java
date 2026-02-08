package javaserver.model.dto;

/**
 * Data Transfer Object for Languages.
 * Used to transfer language information in API responses.
 */
public class LanguagesDTO {

    private Long id;
    private String language;
    private String type;

    public LanguagesDTO() {
    }

/**
     * Constructor for repository queries.
     *
     * @param id the language ID
     * @param language the language name
     * @param type the type of language (e.g., spoken, written)
     */
    public LanguagesDTO(Long id, String language, String type) {
        this.id = id;
        this.language = language;
        this.type = type;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
