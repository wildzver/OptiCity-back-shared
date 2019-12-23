package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.models.Category;
import svidnytskyy.glassesspring.services.CategoryService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"/api/products/categories"})
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

    @GetMapping("/as")
    public Category getCategoryByName (
            @RequestParam String categoryName){
        Category category = categoryService.getCategoryByName(categoryName);
        System.out.println("CATEGORY NAME<>" + category);
        return category;
    }

    @PostMapping("/add")
    public Category addCategory(@RequestPart Category category,
                                @RequestParam MultipartFile categoryImage){
        System.out.println(category);
        return categoryService.save(category, categoryImage);
    }

    @PutMapping("/{id}/update")
    public Category updateCategory(@PathVariable("id") long id,
                                   @RequestPart Category category,
                                   @RequestParam(required = false) MultipartFile categoryImage) throws IOException {
        System.out.println(category);
        return categoryService.update(id, category, categoryImage);
    }

    @DeleteMapping(path =  {"/{id}/delete"})
    public Category delete(@PathVariable("id") long id) {
        return categoryService.delete(id);
    }


}
