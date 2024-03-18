package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HintCreationDTO {

    @NotNull
    private Boolean visible;

    @Size(max = 255)
    @NotBlank
    private String description;

    @NotNull
    private Integer order;


}
