package dev.codebusters.code_busters.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "frontend")
public class FrontendConfig {
    String url;
    String changePasswordPath;
    String confirmRegistrationPath;
}
