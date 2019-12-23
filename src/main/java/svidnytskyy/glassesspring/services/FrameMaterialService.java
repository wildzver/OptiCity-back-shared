package svidnytskyy.glassesspring.services;

import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.FrameMaterialDAO;
import svidnytskyy.glassesspring.models.FrameMaterial;

import java.util.List;

@Service
public class FrameMaterialService {
    private FrameMaterialDAO frameMaterialDAO;

    public FrameMaterialService(FrameMaterialDAO frameMaterialDAO) {
        this.frameMaterialDAO = frameMaterialDAO;
    }

    public FrameMaterial save(FrameMaterial frameMaterial) {
        if(frameMaterial != null) {
            frameMaterialDAO.save(frameMaterial);
        }
        return frameMaterial;
    }

    public List<FrameMaterial> findAll() {return frameMaterialDAO.findAll();}

    public FrameMaterial getOne(Long id) {return frameMaterialDAO.getOne(id);}

    public FrameMaterial update(Long id, FrameMaterial frameMaterial) {
        frameMaterial.setId(id);
        return save(frameMaterial);
    }

    public FrameMaterial delete(Long id) {
        FrameMaterial frameMaterial = getOne(id);
        if(frameMaterial != null){
            frameMaterialDAO.delete(frameMaterial);
        }
        return frameMaterial;
    }
}
