package dev.codebusters.code_busters.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class UserRankingDTO {

    private String name;

    private Long points;

    private String country;

}
