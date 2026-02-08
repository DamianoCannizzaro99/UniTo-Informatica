package javaserver.service;

import javaserver.model.dto.CrewDTO;
import javaserver.repository.CrewRepository;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class CrewService {

    private final CrewRepository crewRepository;

    /**
     * Constructs a CrewService with the specified CrewRepository.
     *
     * @param crewRepository the repository for managing crew
     */
    public CrewService(CrewRepository crewRepository) {
        this.crewRepository = crewRepository;
    }

    public List<CrewDTO> getCrewById(Long id) {
        List<CrewDTO> crew = crewRepository.findCrewById(id);
        if (crew == null || crew.isEmpty()) {
            // Returns null if no crew is found
            return null;
        }
        else return crew;
    }
}
