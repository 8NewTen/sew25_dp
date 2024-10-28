package at.rennweg.htl.yousong.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Getter
@Setter
@Entity
@CrossOrigin(origins = "http://localhost:5173")
@Table(name = "song", schema = "yousong_db")
public class Song {
    @Id
    @Column(name = "SongId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Title", length = 100)
    private String title;

    @Column(name = "Artist", length = 100)
    private String artist;

    @Column(name = "Genre", length = 100)
    private String genre;

    @Column(name = "Length")
    private Integer length;
}