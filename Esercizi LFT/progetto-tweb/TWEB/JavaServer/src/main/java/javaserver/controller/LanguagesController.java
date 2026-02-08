package javaserver.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javaserver.model.dto.LanguagesDTO;
import javaserver.service.LanguagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import java.util.List;


@Tag(name = "Languages", description = "Language management operations")
@RestController
@RequestMapping("/languages")
public class LanguagesController {

    private final LanguagesService languagesService;

    /**
     * Constructs a LanguagesController with the specified LanguagesService.
     *
     * @param languagesService the service for managing languages
     */
    @Autowired
    public LanguagesController(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }

    /**
     * Retrieves languages by the specified ID.
     *
     * @param id the ID of the language
     * @return a ResponseEntity containing a list of languages
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<List<LanguagesDTO>> findLanguages(@PathVariable Long id) {
        List<LanguagesDTO> languages = languagesService.findLanguagesById(id);
        if (languages == null || languages.isEmpty()) {
            // Returns 404 (Not Found) if no languages are found
            return ResponseEntity.notFound().build();
        }
        // Returns 200 (OK) with the list of languages
        return ResponseEntity.ok(languages);
    }


}
