package dev.codebusters.code_busters.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class AppUserDTO {

    private Long id;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String name;

    private OffsetDateTime added;

    private OffsetDateTime lastActive;

    private Boolean enabled;

    private Boolean premium;

    private Long points;

    private Integer profileImage;

    private Long country;

    private Long city;

    private Long userType;

}
