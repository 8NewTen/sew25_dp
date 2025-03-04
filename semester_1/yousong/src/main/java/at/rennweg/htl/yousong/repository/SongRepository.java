package at.rennweg.htl.yousong.repository;

import at.rennweg.htl.yousong.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // Modify method to support pagination
    Page<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseAndGenresIn(
            String title, String artist, List<String> genres, Pageable pageable);

    // Search by genres
    Page<Song> findByGenresIn(List<String> genres, Pageable pageable);

    Page<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(String title, String artist, Pageable pageable);

    @Query("SELECT DISTINCT g FROM Song s JOIN s.genres g")
    List<String> findAllDistinctGenres();


}

