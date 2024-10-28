package at.rennweg.htl.yousong;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "song", schema = "yousong_db")
public class Song {
    @Id
    @Column(name = "SongId", nullable = false)
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