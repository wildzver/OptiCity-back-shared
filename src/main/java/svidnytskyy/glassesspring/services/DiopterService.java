package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.DiopterDAO;
import svidnytskyy.glassesspring.dao.LensColorDAO;
import svidnytskyy.glassesspring.models.Diopter;
import svidnytskyy.glassesspring.models.LensColor;

import java.util.List;

@Service
public class DiopterService {

    @Autowired
    private DiopterDAO diopterDAO;

    public DiopterService(DiopterDAO diopterDAO) {
        this.diopterDAO = diopterDAO;
    }

    public Diopter save(Diopter diopter) {
        if(diopter != null) {
            diopterDAO.save(diopter);
        }
        return diopter;
    }

    public List<Diopter> findAll() {return diopterDAO.findAll();}

    public Diopter getOne(Long id) {return diopterDAO.getOne(id);}

    public Diopter update(Long id, Diopter diopter) {
        diopter.setId(id);
        return save(diopter);
    }

    public Diopter delete(Long id) {
        Diopter diopter = getOne(id);
        if(diopter != null){
            diopterDAO.delete(diopter);
        }
        return diopter;
    }
}
