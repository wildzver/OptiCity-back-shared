package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.Category;

public interface CategoryDAO extends JpaRepository<Category, Long> {
}
