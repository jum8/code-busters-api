package dev.codebusters.code_busters.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;


@Getter
@Setter
public class UserSubscriptionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate startDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate expirationDate;

    @NotNull
    private Long user;

    @NotNull
    private Long subscription;

}
