package at.rennweg.htl.yousong.controller;

import at.rennweg.htl.yousong.model.Song;
import at.rennweg.htl.yousong.repository.SongRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class  nSongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/songs")
    public ResponseEntity<Page<Song>> fetchOrSearchSongs(
            @RequestParam(required = false) String query, //stored query parameter
            @RequestParam(defaultValue = "0") int page, // mapped page query to control which page
            @RequestParam(defaultValue = "5") int size  // mapped size um anzahl an entries pro seite zu definieren
    ) {
        Pageable pageable = PageRequest.of(page, size); //enables pagable objekt
        Page<Song> songsPage;

        if (query != null && !query.isEmpty()) { //checks if the query is not null
            songsPage = songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(query, query, pageable); // called die custom query method,
            // looks for matches in either the title or artist field
        } else {
            songsPage = songRepository.findAll(pageable); //if no found fetch all songs
        }

        return ResponseEntity.ok(songsPage);
    }

    @PostMapping("/songs")
    public ResponseEntity<Song> createSong(@Valid @RequestBody Song song) {
        System.out.println("Received Song: " + song);
        Song createdSong = songRepository.save(song);
        return ResponseEntity.ok(createdSong);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @Valid @RequestBody Song songDetails) {
        Optional<Song> optionalSong = songRepository.findById(id);

        if (!optionalSong.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Song song = optionalSong.get();

        // Update the song properties with the new data
        song.setTitle(songDetails.getTitle());
        song.setArtist(songDetails.getArtist());
        song.setGenre(songDetails.getGenre());
        song.setLength(songDetails.getLength());

        //save song to repo
        Song updatedSong = songRepository.save(song);
        return ResponseEntity.ok(updatedSong);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Optional<Song> optionalSong = songRepository.findById(id);

        if (optionalSong.isPresent()) {
            return ResponseEntity.ok(optionalSong.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") Long id) {
        if (!songRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        songRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
