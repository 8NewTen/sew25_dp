package at.rennweg.htl.yousong.controller;
import at.rennweg.htl.yousong.model.Artist;
import at.rennweg.htl.yousong.repository.ArtistRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
@CrossOrigin(origins = "http://localhost:5173")

public class ArtistController {
    private final ArtistRepository artistRepository;

    public ArtistController(ArtistRepository ArtistRepository) {
        this.artistRepository = ArtistRepository;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(artistRepository.findAll());
    }
}
