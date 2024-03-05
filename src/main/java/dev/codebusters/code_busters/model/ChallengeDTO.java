package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class ChallengeDTO {

    private Long id;

    private OffsetDateTime added;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Boolean exposed;

    @NotNull
    @Size(max = 255)
    private String flag;

    @NotNull
    private Long points;

    @NotNull
    private Long credits;

    @NotNull
    private ChallengeLevel level;

    private String imageUrl;

    @NotNull
    private Boolean premium;

    private Long category;

}
