package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
