package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


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

    @Size(max = 255)
    private String password;

    private Boolean enabled;

    private Long country;

    private Long userType;

}
