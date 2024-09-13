package at.rennweg.htl.yousong.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name ="song_title", nullable = false)
    @JsonProperty("title")
    private String title;

    @Column(name="song_artist")
    @JsonProperty("artist")
    private String artist;

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("length")
    private int length;
}
