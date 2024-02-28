package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryManipulationDTO {

    @Size(max = 255)
    @NotBlank
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    private Boolean exposed;

}
