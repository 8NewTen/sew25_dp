package at.rennweg.htl.yousong.controller;

import at.rennweg.htl.yousong.model.Artist;
import at.rennweg.htl.yousong.model.Song;
import at.rennweg.htl.yousong.repository.ArtistRepository;
import at.rennweg.htl.yousong.repository.SongProjection;
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
@CrossOrigin
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    // Modified to use projections to exclude music data
    @GetMapping("/songs")
    public ResponseEntity<Page<SongProjection>> fetchOrSearchSongs(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SongProjection> songsPage;

        if (query != null && !query.isEmpty()) {
            songsPage = songRepository.findProjectedByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(
                    query, query, pageable);
        } else if (genres != null && !genres.isEmpty()) {
            songsPage = songRepository.findProjectedByGenresIn(genres, pageable);
        } else {
            songsPage = songRepository.findAllProjectedBy(pageable);
        }

        return ResponseEntity.ok(songsPage);
    }

    // Keep the existing endpoints
    @GetMapping("/songs/artists")
    public ResponseEntity<List<Artist>> fetchArtists() {
        return ResponseEntity.ok(artistRepository.findAll());
    }

    @PostMapping("/songs")
    public ResponseEntity<Song> createSong(@Valid @RequestBody Song song) {
        Optional<Artist> optionalArtist = artistRepository.findById(song.getArtist().getId());
        if (optionalArtist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        song.setArtist(optionalArtist.get());
        Song createdSong = songRepository.save(song);
        return ResponseEntity.ok(createdSong);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody Song songDetails,
            @RequestHeader(value = "If-Match", required = false) String ifMatch) {

        System.out.println("Received If-Match: " + ifMatch);


        return songRepository.findById(id).map(song -> {
            // Check if the ETag matches the current version
            String currentVersion = Long.toString(song.getVersion());
            if (ifMatch != null && !ifMatch.replace("\"", "").equals(currentVersion)) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).<Song>build();
            }

            // Update song fields
            song.setTitle(songDetails.getTitle());
            song.setGenres(songDetails.getGenres());
            song.setLength(songDetails.getLength());

            // Preserve music data if not provided in the update
            if (songDetails.getMusicData() != null) {
                song.setMusicData(songDetails.getMusicData());
            }

            Optional<Artist> optionalArtist = artistRepository.findById(songDetails.getArtist().getId());
            optionalArtist.ifPresent(song::setArtist);

            // Save and return with new ETag
            Song savedSong = songRepository.save(song);
            return ResponseEntity.ok()
                    .eTag(Long.toString(savedSong.getVersion()))
                    .body(savedSong);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SongProjection> getSongById(@PathVariable Long id) {
        SongProjection song = songRepository.findProjectedById(id);
        return song != null ? ResponseEntity.ok().eTag(Long.toString(song.getVersion())).body(song) : ResponseEntity.notFound().build();
    }

    @GetMapping("/songs/{id}/play")
    public ResponseEntity<Song> getSongWithMusicData(@PathVariable Long id) {
        return songRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") Long id) {
        if (!songRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        songRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> fetchGenres() {
        return ResponseEntity.ok(songRepository.findAllDistinctGenres());
    }
}