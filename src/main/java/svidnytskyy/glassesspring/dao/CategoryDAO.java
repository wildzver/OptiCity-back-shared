package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import svidnytskyy.glassesspring.models.Category;

import java.util.List;

public interface CategoryDAO extends JpaRepository<Category, Long> {
    Category getCategoryById(long id);

    @Query("select c from Category c where c.name=:categoryName")
    Category findFirstByName(@Param("categoryName") String categoryName);
}
