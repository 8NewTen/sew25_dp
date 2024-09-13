package at.rennweg.htl.yousong.repository;

import at.rennweg.htl.yousong.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {

}
