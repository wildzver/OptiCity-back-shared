package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.Diopter;
import svidnytskyy.glassesspring.models.FrameMaterial;
import svidnytskyy.glassesspring.services.DiopterService;

import java.util.List;

@RestController
@RequestMapping({"/api/products/diopters"})
public class DiopterController {
    @Autowired
    DiopterService diopterService;

    @GetMapping
    public List<Diopter> findAll() {
        System.out.println(diopterService.findAll());
        return diopterService.findAll();
    }

    @GetMapping("/{id}")
    public Diopter getOne(@PathVariable("id") long id) {
        return diopterService.getOne(id);
    }

    @PostMapping("/add")
    public Diopter addFrameMaterial(@RequestBody Diopter diopter) {
        System.out.println(diopter);
        return diopterService.save(diopter);
    }

    @PutMapping("/{id}/update")
    public Diopter updateDiopter(@PathVariable("id") long id,
                                 @RequestBody Diopter diopter) {
        System.out.println(diopter);
        return diopterService.update(id, diopter);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public Diopter delete(@PathVariable("id") long id) {
        return diopterService.delete(id);
    }
}
