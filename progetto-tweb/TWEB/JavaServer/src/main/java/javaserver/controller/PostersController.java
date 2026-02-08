package javaserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import javaserver.model.dto.PostersDTO;
import javaserver.service.PostersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Tag(name = "Posters", description = "Movie poster management operations")
@RestController
@RequestMapping("/posters")
public class PostersController {

    private final PostersService postersService;

    @Autowired
    public PostersController(PostersService postersService) {
        this.postersService = postersService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PostersDTO> getPosterById(@PathVariable Long id) {
        PostersDTO poster = postersService.getPosterById(id);
        if (poster == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(poster);
    }
}
