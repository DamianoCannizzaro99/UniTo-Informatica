package javaserver.service;

import javaserver.model.dto.LanguagesDTO;
import javaserver.repository.LanguagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LanguagesService {

    private final LanguagesRepository languagesRepository;


    /**
     * Constructs a LanguagesService with the specified LanguagesRepository.
     *
     * @param languagesRepository the repository for managing languages
     */
    @Autowired
    public LanguagesService(LanguagesRepository languagesRepository) {
        this.languagesRepository = languagesRepository;
    }


    public List<LanguagesDTO> findLanguagesById(Long id) {
        return languagesRepository.findLanguagesById(id);
    }
    // TODO: Implement the service logic for handling languages
}
