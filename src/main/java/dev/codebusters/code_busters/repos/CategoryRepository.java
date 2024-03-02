package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByExposedTrue();
}
