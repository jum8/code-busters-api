package dev.codebusters.code_busters.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HintDTO {

    private Long id;

    private Boolean visible;

    @Size(max = 255)
    private String description;

    private Integer order;

    private Long challenge;

}
