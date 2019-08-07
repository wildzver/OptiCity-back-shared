package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.FrameColor;
import svidnytskyy.glassesspring.services.FrameColorService;
import java.util.List;

@RestController
@RequestMapping({"/products/frame-colors"})
public class FrameColorController {
    @Autowired
    FrameColorService frameColorService;

    @GetMapping
    public List<FrameColor> findAll(){
        System.out.println(frameColorService.findAll());
        return frameColorService.findAll();
    }

    @GetMapping("/{id}")
    public FrameColor getOne(@PathVariable("id") long id){
        return frameColorService.getOne(id);
    }

    @PostMapping("/add")
    public FrameColor addColor(@RequestBody FrameColor frameColor){
        System.out.println(frameColor);
        return frameColorService.save(frameColor);
    }

    @PutMapping("/{id}/update")
    public FrameColor updateColor(@PathVariable("id") long id, @RequestBody FrameColor frameColor) {
        System.out.println(frameColor);
        return frameColorService.update(id, frameColor);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public FrameColor delete(@PathVariable("id") long id) {
        return frameColorService.delete(id);
    }


}
