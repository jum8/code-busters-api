package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CountryDTO {

    private Long id;

    @Size(max = 255)
    private String countryName;

    @Size(max = 255)
    private String countryCode;

}
