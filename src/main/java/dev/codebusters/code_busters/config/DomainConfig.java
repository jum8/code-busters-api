package dev.codebusters.code_busters.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("dev.codebusters.code_busters.domain")
@EnableJpaRepositories("dev.codebusters.code_busters.repos")
@EnableTransactionManagement
public class DomainConfig {
}
