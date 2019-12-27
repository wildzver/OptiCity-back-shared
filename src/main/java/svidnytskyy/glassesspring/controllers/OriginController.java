package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.Origin;
import svidnytskyy.glassesspring.services.OriginService;

import java.util.List;

@RestController
@RequestMapping({"/api/products/origins"})
public class OriginController {
    @Autowired
    OriginService originService;

    @GetMapping
    public List<Origin> findAll() {
        return originService.findAll();
    }

    @GetMapping("/{id}")
    public Origin getOne(@PathVariable("id") long id) {
        return originService.getOne(id);
    }

    @PostMapping("/add")
    public Origin addOrigin(@RequestBody Origin origin) {
        return originService.save(origin);
    }

    @PutMapping("/{id}/update")
    public Origin updateOrigin(@PathVariable("id") long id, @RequestBody Origin origin) {
        return originService.update(id, origin);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public Origin delete(@PathVariable("id") long id) {
        return originService.delete(id);
    }
}
