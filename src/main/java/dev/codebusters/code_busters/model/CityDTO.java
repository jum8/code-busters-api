package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CityDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String cityName;

    @Size(max = 255)
    private String cityCode;

    private Long country;

}