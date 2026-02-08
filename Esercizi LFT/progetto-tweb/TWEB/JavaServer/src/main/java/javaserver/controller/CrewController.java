package javaserver.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import javaserver.model.dto.CrewDTO;
import javaserver.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Crew", description = "Crew management operations")
@RestController
@RequestMapping("/crew")
public class CrewController {

    private final CrewService crewService;

    /**
     * Constructs a CrewController with the specified CrewService.
     *
     * @param crewService the service for managing crew
     */
    @Autowired
    public CrewController(CrewService crewService) {
        this.crewService = crewService;
    }

    /**
     * Retrieves crew members by the specified ID.
     *
     * @param id the ID of the crew
     * @return a ResponseEntity containing a list of crew members
     */
    @GetMapping("id/{id}")
    public ResponseEntity<List<CrewDTO>> getCrewById(@PathVariable Long id) {
        List<CrewDTO> crew = crewService.getCrewById(id);
        if (crew == null || crew.isEmpty()) {
            // Returns 404 (Not Found) if no crew is found
            return ResponseEntity.notFound().build();
        }
        // Returns 200 (OK) with the list of crew members
        return ResponseEntity.ok(crew);
    }

}
