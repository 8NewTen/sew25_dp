package at.rennweg.htl.yousong.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Artist name is required")
    @Size(max = 100, message = "Artist name must be less than or equal to 100 characters")
    @Column(unique = true)  // damit sind ide artist names unique
    private String name;

   // @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Song> songs;  // 1 Artist hat mehrere Songs
}
