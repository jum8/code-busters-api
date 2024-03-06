package dev.codebusters.code_busters.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class ChallengeUpdateDTO {

    @NotBlank
    @Size(max = 255)
    @JsonView({ChallengeView.GetById.class})
    private String title;

    @NotNull
    @JsonView({ChallengeView.GetById.class})
    private String description;

    @NotNull
    @JsonView({ChallengeView.GetByIdAdmin.class})
    private Boolean exposed;

    @NotBlank
    @Size(max = 255)
    @JsonView({ChallengeView.GetByIdAdmin.class})
    private String flag;

    @NotNull
    @JsonView({ChallengeView.GetById.class})
    private Long points;

    @NotNull
    @JsonView({ChallengeView.GetById.class})
    private Long credits;

    @NotNull
    @JsonView({ChallengeView.GetById.class})
    private ChallengeLevel level;

    @Size(max = 255)
    @JsonView({ChallengeView.GetById.class})
    private String imageUrl;

    @NotNull
    @JsonView({ChallengeView.GetById.class})
    private Boolean premium;

    @JsonView({ChallengeView.GetById.class})
    private Long category;

    @JsonView({ChallengeView.GetById.class})
    private Set<@Valid HintUpdateDTO> hints;

}
