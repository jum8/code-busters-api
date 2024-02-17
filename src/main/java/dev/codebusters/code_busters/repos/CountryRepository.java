package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CountryRepository extends JpaRepository<Country, Long> {
}
