package at.rennweg.htl.yousong.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@CrossOrigin(origins = "http://localhost:5173")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")  // Ensure title is not null or empty
    @Size(max = 100, message = "Title must be less than or equal to 100 characters")  // Limit title length
    private String title;

    @ManyToOne  // Viele Songs können zu einem Artist gehören
    //@JoinColumn(name = "artist_id", nullable = false)  // Fremdschlüssel in der Song-Tabelle
    private Artist artist;

    @ElementCollection
    @CollectionTable(name = "song_genre", joinColumns = @JoinColumn(name = "song_id"))
    @Column(name = "genre")
    private List<String> genres;  // A song can have multiple genres (List of Strings)

    @NotBlank
    @Positive(message = "Length must be a positive number")
    private Integer length;
}
