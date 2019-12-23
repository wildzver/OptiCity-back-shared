package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.OriginDAO;
import svidnytskyy.glassesspring.dao.SexDAO;
import svidnytskyy.glassesspring.models.Origin;
import svidnytskyy.glassesspring.models.Sex;

import java.util.List;

@Service
public class SexService {
    @Autowired
    private SexDAO sexDAO;

    public SexService(SexDAO sexDAO) {
        this.sexDAO = sexDAO;
    }

    public Sex save(Sex sex) {
        if(sex != null) {
            sexDAO.save(sex);
        }
        return sex;
    }

    public List<Sex> findAll() {return sexDAO.findAll();}

    public Sex getOne(Long id) {return sexDAO.getOne(id);}

    public Sex update(Long id, Sex sex) {
        sex.setId(id);
        return save(sex);
    }

    public Sex delete(Long id) {
        Sex sex = getOne(id);
        if(sex != null){
            sexDAO.delete(sex);
        }
        return sex;
    }
}
