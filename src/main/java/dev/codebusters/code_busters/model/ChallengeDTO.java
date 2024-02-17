package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChallengeDTO {

    private Long id;

    private OffsetDateTime added;

    @Size(max = 255)
    private String title;

    private String description;

    private Boolean exposed;

    @Size(max = 255)
    private String flag;

    private Integer points;

    private Long category;

}
