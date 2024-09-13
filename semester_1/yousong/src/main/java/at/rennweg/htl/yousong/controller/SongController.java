package at.rennweg.htl.yousong.controller;

import at.rennweg.htl.yousong.model.Song;
import at.rennweg.htl.yousong.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/songs")
    public List<Song> fetchSongs() {
        return songRepository.findAll();
    }
}
