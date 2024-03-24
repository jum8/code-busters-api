package dev.codebusters.code_busters.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubmissionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    private Long user;
    @NotNull
    private Long challenge;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String flag;

}
