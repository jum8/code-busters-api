package dev.codebusters.code_busters.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChallengeSummaryDTO {

    private Long id;

    private String title;

    private Boolean exposed;

    private Integer points;

    private Long credits;

    private ChallengeLevel level;

    private String category;

}
