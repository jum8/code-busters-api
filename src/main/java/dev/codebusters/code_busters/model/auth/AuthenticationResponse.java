package dev.codebusters.code_busters.model.auth;

import dev.codebusters.code_busters.domain.Submission;
import dev.codebusters.code_busters.domain.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class AuthenticationResponse {
    private String email;
    private String name;
    private Boolean premium;
    private Long points;
    private Integer profileImage;
    private Collection<Long> challengesSolved;
    private String userType;

    private String accessToken;
}
