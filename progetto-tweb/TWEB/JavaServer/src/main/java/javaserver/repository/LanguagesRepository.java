package javaserver.repository;

import javaserver.model.dto.LanguagesDTO;
import javaserver.model.entity.Languages;
import javaserver.model.ids.LanguagesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LanguagesRepository extends JpaRepository<Languages, LanguagesId> {

    /**
     * Finds languages by the specified language ID.
     *
     * @param id the ID of the language
     * @return a list of LanguagesDTO objects associated with the specified language ID
     */
    @Query("SELECT new javaserver.model.dto.LanguagesDTO(l.id, l.language, l.type) FROM Languages l WHERE l.id = :id")
    List<LanguagesDTO> findLanguagesById(@Param("id")Long id);
}
