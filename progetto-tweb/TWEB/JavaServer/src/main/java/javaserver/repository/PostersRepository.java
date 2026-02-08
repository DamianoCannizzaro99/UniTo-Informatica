package javaserver.repository;

import javaserver.model.entity.Posters;
import javaserver.model.ids.PostersId;
import javaserver.model.dto.PostersDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostersRepository extends JpaRepository<Posters, PostersId> {



    @Query("""
    SELECT new javaserver.model.dto.PostersDTO(p.id, p.link)
    FROM Posters p
    WHERE p.id = :id
    """)
    PostersDTO findPosterById(Long id);
}
