package svidnytskyy.glassesspring.services;

import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.LensMaterialDAO;
import svidnytskyy.glassesspring.models.LensMaterial;

import java.util.List;

@Service
public class LensMaterialService {
    private LensMaterialDAO lensMaterialDAO;

    public LensMaterialService(LensMaterialDAO lensMaterialDAO) {
        this.lensMaterialDAO = lensMaterialDAO;
    }

    public LensMaterial save(LensMaterial lensMaterial) {
        if(lensMaterial != null) {
            lensMaterialDAO.save(lensMaterial);
        }
        return lensMaterial;
    }

    public List<LensMaterial> findAll() {return lensMaterialDAO.findAll();}

    public LensMaterial getOne(Long id) {return lensMaterialDAO.getOne(id);}

    public LensMaterial update(Long id, LensMaterial lensMaterial) {
        lensMaterial.setId(id);
        return save(lensMaterial);
    }

    public LensMaterial delete(Long id) {
        LensMaterial lensMaterial = getOne(id);
        if(lensMaterial != null){
            lensMaterialDAO.delete(lensMaterial);
        }
        return lensMaterial;
    }
}
