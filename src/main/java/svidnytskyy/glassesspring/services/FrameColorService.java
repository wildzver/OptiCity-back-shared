package svidnytskyy.glassesspring.services;

import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.FrameColorDAO;
import svidnytskyy.glassesspring.models.FrameColor;

import java.util.List;

@Service
public class FrameColorService {
    private FrameColorDAO frameColorDAO;

    public FrameColorService(FrameColorDAO frameColorDAO) {
        this.frameColorDAO = frameColorDAO;
    }

    public FrameColor save(FrameColor frameColor) {
        if(frameColor != null) {
            frameColorDAO.save(frameColor);
        }
        return frameColor;
    }

    public List<FrameColor> findAll() {return frameColorDAO.findAll();}

    public FrameColor getOne(Long id) {return frameColorDAO.getOne(id);}

    public FrameColor update(Long id, FrameColor frameColor) {
        getOne(id);
        frameColor.setId(id);
        return save(frameColor);
    }

    public FrameColor delete(Long id) {
        FrameColor frameColor = getOne(id);
        if(frameColor != null){
            frameColorDAO.delete(frameColor);
        }
        return frameColor;
    }
}
