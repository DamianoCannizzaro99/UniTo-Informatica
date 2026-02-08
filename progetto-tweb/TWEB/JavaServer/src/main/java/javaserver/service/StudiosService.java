package javaserver.service;

import javaserver.model.dto.StudiosDTO;
import javaserver.repository.StudiosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudiosService {

    private final StudiosRepository studiosRepository;

    /**
     * Constructor for StudiosService.
     *
     * @param studiosRepository the repository for Studios entities
     */
    @Autowired
    public StudiosService(StudiosRepository studiosRepository) {
        this.studiosRepository = studiosRepository;
    }


    public List<StudiosDTO> getAllStudios() {
        return studiosRepository.findAllStudios();
    }

    public List<StudiosDTO> getStudiosByMovieId(Long movieId) {
        return studiosRepository.findStudiosByMovieId(movieId);
    }

    public Integer getNumFilmsByStudio(String studioName) {
        return studiosRepository.countFilmsByStudio(studioName);
    }

}
