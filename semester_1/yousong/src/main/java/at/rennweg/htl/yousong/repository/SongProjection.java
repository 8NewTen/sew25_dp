package at.rennweg.htl.yousong.repository;

import at.rennweg.htl.yousong.model.Artist;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
public interface SongProjection {
    Long getId();
    String getTitle();
    Artist getArtist();
    List<String> getGenres();
    Integer getLength();
    Long getVersion();
}
