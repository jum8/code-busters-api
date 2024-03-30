package dev.codebusters.code_busters.model;

import dev.codebusters.code_busters.domain.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private AppUser user;
    private String appUrl;
    public OnRegistrationCompleteEvent(AppUser user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
}
