package svidnytskyy.glassesspring.services;

import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.CategoryDAO;
import svidnytskyy.glassesspring.models.Category;

import java.util.List;

@Service
public class CategoryService {
    private CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public Category save(Category category) {
        if(category != null) {
            categoryDAO.save(category);
        }
        return category;
    }

    public List<Category> findAll() {return categoryDAO.findAll();}

    public Category getOne(long id) {return categoryDAO.getOne(id);}

    public Category update(long id, Category category) {
        getOne(id);
        category.setId(id);
        return save(category);
    }

    public Category delete(long id) {
        Category category = getOne(id);
        if(category != null){
            categoryDAO.delete(category);
        }
        return category;
    }

}
