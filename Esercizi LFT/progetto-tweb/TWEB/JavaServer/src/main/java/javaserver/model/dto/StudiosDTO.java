package javaserver.model.dto;



public class StudiosDTO {

    private Long id; // Foreign key referring to Movies.id
    private String studio; // Name of the studio


    public StudiosDTO(Long id, String studio) {
        this.id = id;
        this.studio = studio;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getStudio() {
        return studio;
    }
    public void setStudio(String studio) {
        this.studio = studio;
    }


}
