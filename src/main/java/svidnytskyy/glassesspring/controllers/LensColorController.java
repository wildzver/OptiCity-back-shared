package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.models.LensColor;
import svidnytskyy.glassesspring.services.LensColorService;

import java.util.List;

@RestController
@RequestMapping({"/api/products/lens-colors"})
public class LensColorController {
    @Autowired
    LensColorService lensColorService;

    @GetMapping
    public List<LensColor> findAll(){
        return lensColorService.findAll();
    }

    @GetMapping("/{id}")
    public LensColor getOne(@PathVariable("id") long id){
        return lensColorService.getOne(id);
    }

    @PostMapping("/add")
    public LensColor addColor(@RequestPart LensColor lensColor,
                              @RequestParam MultipartFile lensColorImage){
        return lensColorService.save(lensColor, lensColorImage);
    }

    @PutMapping("/{id}/update")
    public LensColor updateColor(@PathVariable("id") long id,
                                 @RequestPart LensColor lensColor,
                                 @RequestParam(required = false) MultipartFile lensColorImage) {
        return lensColorService.update(id, lensColor, lensColorImage);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public LensColor delete(@PathVariable("id") long id) {
        return lensColorService.delete(id);
    }


}
