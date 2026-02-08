package javaserver.repository;

import javaserver.model.dto.ThemesDTO;
import javaserver.model.entity.Themes;
import javaserver.model.ids.ThemesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThemesRepository extends JpaRepository<Themes, ThemesId> {

    /**
     * Finds themes by the specified ID.
     *
     * @param id the ID of the theme
     * @return a list of ThemesDTO objects associated with the specified ID
     */
    @Query("SELECT new javaserver.model.dto.ThemesDTO(t.id, t.theme) FROM Themes t WHERE t.id = :id")
    List<ThemesDTO> findTemesById(@Param("id")long id);
}
