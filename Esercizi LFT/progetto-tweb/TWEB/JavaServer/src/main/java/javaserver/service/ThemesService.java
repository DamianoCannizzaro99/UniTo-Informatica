package javaserver.service;

import javaserver.model.dto.ThemesDTO;
import javaserver.repository.ThemesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ThemesService {

    private final ThemesRepository themesRepository;

    /**
     * Constructs a ThemesService with the specified ThemesRepository.
     *
     * @param themesRepository the repository for managing themes
     */
    @Autowired
    public ThemesService(ThemesRepository themesRepository) {
        this.themesRepository = themesRepository;
    }

    /**
     * Retrieves themes by the specified ID.
     *
     * @param id the ID of the theme
     * @return a list of ThemesDTO objects associated with the specified ID
     */
    public List<ThemesDTO> getThemesById(long id) {
        return themesRepository.findTemesById(id);
    }
}
