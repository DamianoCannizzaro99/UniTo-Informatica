package javaserver.repository;

import javaserver.model.dto.CrewDTO;
import javaserver.model.entity.Crew;
import javaserver.model.ids.CrewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrewRepository extends JpaRepository<Crew, CrewId> {


    @Query("SELECT new javaserver.model.dto.CrewDTO(c.id, c.name, c.role) " +
           "FROM Crew c " +
           "WHERE c.id = :id")
    List<CrewDTO> findCrewById(@Param("id")Long id);
}
