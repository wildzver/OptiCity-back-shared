package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.Category;
import svidnytskyy.glassesspring.services.CategoryService;
import java.util.List;

@RestController
@RequestMapping({"/products/categories"})
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping
    public List<Category> findAll(){
        System.out.println(categoryService.findAll());
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category getOne(@PathVariable("id") long id){
        return categoryService.getOne(id);
    }

    @PostMapping("/add")
    public Category addCategory(@RequestBody Category category){
        System.out.println(category);
        return categoryService.save(category);
    }

    @PutMapping("/{id}/update")
    public Category updateCategory(@PathVariable("id") long id, @RequestBody Category category) {
        System.out.println(category);
        return categoryService.update(id, category);
    }

    @DeleteMapping(path =  {"/{id}/delete"})
    public Category delete(@PathVariable("id") long id) {
        return categoryService.delete(id);
    }


}
