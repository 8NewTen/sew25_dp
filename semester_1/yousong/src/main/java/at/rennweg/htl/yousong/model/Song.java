package at.rennweg.htl.yousong.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Title is required")  // Ensure title is not null or empty
    @Size(max = 100, message = "Title must be less than or equal to 100 characters")  // Limit title length
    @Column(name = "Title", length = 100)
    private String title;

    @NotBlank(message = "Artist is required")  // Ensure artist is not null or empty
    @Size(max = 100, message = "Artist must be less than or equal to 100 characters")  // Limit artist length
    @Column(name = "Artist", length = 100)
    private String artist;

    @NotBlank(message = "Genre is required")  // Ensure genre is not null or empty
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Genre must contain only alphabetic characters and spaces.")
    @Size(max = 100, message = "Genre must be less than or equal to 100 characters")  // Limit genre length
    @Column(name = "Genre", length = 100)
    private String genre;

    @NotNull(message = "Length is required")  // Ensure length is not null
    @Column(name = "Length")
    private Integer length;
}
