package at.rennweg.htl.yousong.controller;

import at.rennweg.htl.yousong.model.Song;
import at.rennweg.htl.yousong.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/songs")
    public List<Song> fetchSongs() {
        return songRepository.findAll();
    }

    @PostMapping("/songs")
    public ResponseEntity<Song>  createSong(@RequestBody Song song) {
        System.out.println("Received Song: " + song);
        Song createdSong = songRepository.save(song);
        return ResponseEntity.ok(createdSong);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        // Fetch the song by ID from the repository
        Optional<Song> optionalSong = songRepository.findById(id);

        if (!optionalSong.isPresent()) {
            return ResponseEntity.notFound().build(); // If song not found, return 404
        }

        Song song = optionalSong.get();

        // Update the song properties with the new data
        song.setTitle(songDetails.getTitle());
        song.setArtist(songDetails.getArtist());
        song.setGenre(songDetails.getGenre());
        song.setLength(songDetails.getLength());

        // Save the updated song to the repository
        Song updatedSong = songRepository.save(song);

        // Return the updated song as response
        return ResponseEntity.ok(updatedSong);
    }

    // (Optional) Fetch a single song by its ID
    @GetMapping("/songs/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Optional<Song> optionalSong = songRepository.findById(id);

        if (optionalSong.isPresent()) {
            return ResponseEntity.ok(optionalSong.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
