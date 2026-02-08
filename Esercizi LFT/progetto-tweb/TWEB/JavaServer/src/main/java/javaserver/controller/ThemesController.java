package javaserver.controller;


import javaserver.model.dto.ThemesDTO;
import javaserver.service.ThemesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing themes.
 */
@RestController
@RequestMapping("/themes")
public class ThemesController {
    private final ThemesService themesService;

    /**
     * Constructs a ThemesController with the specified ThemesService.
     *
     * @param themesService the service for managing themes
     */
    @Autowired
    public ThemesController(ThemesService themesService) {
        this.themesService = themesService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<List<ThemesDTO>> getThemesById(@PathVariable long id){
        List<ThemesDTO> themes = themesService.getThemesById(id);
        if (themes == null || themes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(themes);
    }

}
