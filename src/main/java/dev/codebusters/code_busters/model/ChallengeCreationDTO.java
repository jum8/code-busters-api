package dev.codebusters.code_busters.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class ChallengeCreationDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Boolean exposed;

    @NotBlank
    @Size(max = 255)
    private String flag;

    @NotNull
    private Long points;

    @NotNull
    private Long credits;

    @NotNull
    private ChallengeLevel level;

    @Size(max = 255)
    private String imageUrl;

    @NotNull
    private Boolean premium;

    private Long category;

    private Set<@Valid HintCreationDTO> hints;

}
