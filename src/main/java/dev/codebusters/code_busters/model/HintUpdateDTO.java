package dev.codebusters.code_busters.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HintUpdateDTO {

    @JsonView({ChallengeView.GetByIdAdmin.class})
    private Long id;

    @NotNull
    @JsonView({ChallengeView.GetById.class})
    private Boolean visible;

    @Size(max = 255)
    @NotBlank
    @JsonView({ChallengeView.GetById.class})
    private String description;

    @NotNull
    @JsonView({ChallengeView.GetById.class})
    private Integer order;
}
