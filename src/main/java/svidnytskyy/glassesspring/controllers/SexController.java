package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.Origin;
import svidnytskyy.glassesspring.models.Sex;
import svidnytskyy.glassesspring.services.SexService;

import java.util.List;

@RestController
@RequestMapping({"/api/products/sex"})
public class SexController {
    @Autowired
    SexService sexService;

    @GetMapping
    public List<Sex> findAll() {
        System.out.println(sexService.findAll());
        return sexService.findAll();
    }

    @GetMapping("/{id}")
    public Sex getOne(@PathVariable("id") long id) {
        return sexService.getOne(id);
    }

    @PostMapping("/add")
    public Sex addSex(@RequestBody Sex sex) {
        System.out.println(sex);
        return sexService.save(sex);
    }

    @PutMapping("/{id}/update")
    public Sex updateSex(@PathVariable("id") long id, @RequestBody Sex sex) {
        System.out.println(sex);
        return sexService.update(id, sex);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public Sex delete(@PathVariable("id") long id) {
        return sexService.delete(id);
    }
}
