package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.FrameMaterial;
import svidnytskyy.glassesspring.services.FrameMaterialService;

import java.util.List;

@RestController
@RequestMapping({"/api/products/frame-materials"})
public class FrameMaterialController {
    @Autowired
    FrameMaterialService frameMaterialService;

    @GetMapping
    public List<FrameMaterial> findAll() {
        System.out.println(frameMaterialService.findAll());
        return frameMaterialService.findAll();
    }

    @GetMapping("/{id}")
    public FrameMaterial getOne(@PathVariable("id") long id) {
        return frameMaterialService.getOne(id);
    }

    @PostMapping("/add")
    public FrameMaterial addFrameMaterial(@RequestBody FrameMaterial frameMaterial) {
        return frameMaterialService.save(frameMaterial);
    }

    @PutMapping("/{id}/update")
    public FrameMaterial updateFrameMaterial(@PathVariable("id") long id,
                                             @RequestBody FrameMaterial frameMaterial) {
        return frameMaterialService.update(id, frameMaterial);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public FrameMaterial delete(@PathVariable("id") long id) {
        return frameMaterialService.delete(id);
    }
}
