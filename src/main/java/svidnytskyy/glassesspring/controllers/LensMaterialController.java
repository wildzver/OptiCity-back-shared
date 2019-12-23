package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.LensMaterial;
import svidnytskyy.glassesspring.services.LensMaterialService;

import java.util.List;

@RestController
@RequestMapping({"/api/products/lens-materials"})
public class LensMaterialController {
    @Autowired
    LensMaterialService lensMaterialService;

    @GetMapping
    public List<LensMaterial> findAll() {
        System.out.println(lensMaterialService.findAll());
        return lensMaterialService.findAll();
    }

    @GetMapping("/{id}")
    public LensMaterial getOne(@PathVariable("id") long id) {
        return lensMaterialService.getOne(id);
    }

    @PostMapping("/add")
    public LensMaterial addLensMaterial(@RequestBody LensMaterial lensMaterial) {
        System.out.println(lensMaterial);
        return lensMaterialService.save(lensMaterial);
    }

    @PutMapping("/{id}/update")
    public LensMaterial updateLensMaterial(@PathVariable("id") long id,
                                           @RequestBody LensMaterial lensMaterial) {
        System.out.println(lensMaterial);
        return lensMaterialService.update(id, lensMaterial);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public LensMaterial delete(@PathVariable("id") long id) {
        return lensMaterialService.delete(id);
    }
}
