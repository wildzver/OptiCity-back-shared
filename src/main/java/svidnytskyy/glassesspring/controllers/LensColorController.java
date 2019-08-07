package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.LensColor;
import svidnytskyy.glassesspring.services.LensColorService;

import java.util.List;

@RestController
@RequestMapping({"/products/lens-colors"})
public class LensColorController {
    @Autowired
    LensColorService lensColorService;

    @GetMapping
    public List<LensColor> findAll(){
        System.out.println(lensColorService.findAll());
        return lensColorService.findAll();
    }

    @GetMapping("/{id}")
    public LensColor getOne(@PathVariable("id") long id){
        return lensColorService.getOne(id);
    }

    @PostMapping("/add")
    public LensColor addColor(@RequestBody LensColor lensColor){
        System.out.println(lensColor);
        return lensColorService.save(lensColor);
    }

    @PutMapping("/{id}/update")
    public LensColor updateColor(@PathVariable("id") long id, @RequestBody LensColor lensColor) {
        System.out.println(lensColor);
        return lensColorService.update(id, lensColor);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public LensColor delete(@PathVariable("id") long id) {
        return lensColorService.delete(id);
    }


}
