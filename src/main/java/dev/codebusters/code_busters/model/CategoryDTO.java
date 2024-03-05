package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryDTO {

    private Long id;

    private OffsetDateTime added;

    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String imageUrl;

    private Boolean exposed;

}
