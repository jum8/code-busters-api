package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


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

    private Long category;

}
