package dev.codebusters.code_busters.model.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
public class JwtUserDetails extends User {
    public final Long id;
    private String email;
    private String name;
    private Boolean premium;
    private Long points;
    private Integer profileImage;
    private Collection<Long> challengesSolved;
    private String userType;

    public JwtUserDetails(final Long id, final String email, final String name, final Boolean premium,
                          final Long points, final Integer profileImage, final Collection<Long> challengesSolved,
                          final String userType, final String username, final String hash,
                          final boolean enabled, final boolean accountNonExpired, final boolean credentialsNonExpired,
                          final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities) {
        super(username, hash, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.email = email;
        this.name = name;
        this.premium = premium;
        this.points = points;
        this.profileImage = profileImage;
        this.challengesSolved = challengesSolved;
        this.userType = userType;
    }
}
