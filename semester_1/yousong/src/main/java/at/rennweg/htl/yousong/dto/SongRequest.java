package at.rennweg.htl.yousong.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SongRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Artist name is required")
    private String artistName;

    private List<String> genres;

    @Positive(message = "Length must be a positive number")
    private Integer length;
}
