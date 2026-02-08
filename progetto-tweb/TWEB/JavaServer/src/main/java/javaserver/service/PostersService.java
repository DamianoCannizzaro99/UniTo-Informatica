package javaserver.service;

import javaserver.model.dto.PostersDTO;
import javaserver.repository.PostersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
public class PostersService {

    private final PostersRepository postersRepository;

    /**
     * Constructs a PostersService with the specified PostersRepository.
     *
     * @param postersRepository the repository for managing posters
     */
    @Autowired
    public PostersService(PostersRepository postersRepository) {
        this.postersRepository = postersRepository;
    }


    public PostersDTO getPosterById(Long id) {
        return postersRepository.findPosterById(id);
    }
}
