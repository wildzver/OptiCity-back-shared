package svidnytskyy.glassesspring.services;

import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.LensColorDAO;
import svidnytskyy.glassesspring.models.LensColor;

import java.util.List;

@Service
public class LensColorService {
    private LensColorDAO lensColorDAO;

    public LensColorService(LensColorDAO lensColorDAO) {
        this.lensColorDAO = lensColorDAO;
    }

    public LensColor save(LensColor lensColor) {
        if(lensColor != null) {
            lensColorDAO.save(lensColor);
        }
        return lensColor;
    }

    public List<LensColor> findAll() {return lensColorDAO.findAll();}

    public LensColor getOne(Long id) {return lensColorDAO.getOne(id);}

    public LensColor update(Long id, LensColor lensColor) {
        getOne(id);
        lensColor.setId(id);
        return save(lensColor);
    }

    public LensColor delete(Long id) {
        LensColor lensColor = getOne(id);
        if(lensColor != null){
            lensColorDAO.delete(lensColor);
        }
        return lensColor;
    }
}
