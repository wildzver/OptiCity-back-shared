package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.models.FrameColor;
import svidnytskyy.glassesspring.services.FrameColorService;
import java.util.List;

@RestController
@RequestMapping({"/api/products/frame-colors"})
public class FrameColorController {
    @Autowired
    FrameColorService frameColorService;

    @GetMapping
    public List<FrameColor> findAll(){
        return frameColorService.findAll();
    }

    @GetMapping("/{id}")
    public FrameColor getOne(@PathVariable("id") long id){
        return frameColorService.getOne(id);
    }

    @PostMapping("/add")
    public FrameColor addColor(@RequestPart FrameColor frameColor,
                               @RequestParam MultipartFile frameColorImage){
        return frameColorService.save(frameColor, frameColorImage);
    }

    @PutMapping("/{id}/update")
    public FrameColor updateColor(@PathVariable("id") long id,
                                  @RequestPart FrameColor frameColor,
                                  @RequestParam(required = false) MultipartFile frameColorImage) {
        return frameColorService.update(id, frameColor, frameColorImage);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public FrameColor delete(@PathVariable("id") long id) {
        return frameColorService.delete(id);
    }
}
