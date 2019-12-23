package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.OriginDAO;
import svidnytskyy.glassesspring.models.Origin;

import java.util.List;

@Service
public class OriginService {
    @Autowired
    private OriginDAO originDAO;

    public OriginService(OriginDAO originDAO) {
        this.originDAO = originDAO;
    }

    public Origin save(Origin origin) {
        if(origin != null) {
            originDAO.save(origin);
        }
        return origin;
    }

    public List<Origin> findAll() {return originDAO.findAll();}

    public Origin getOne(Long id) {return originDAO.getOne(id);}

    public Origin update(Long id, Origin origin) {
        origin.setId(id);
        return save(origin);
    }

    public Origin delete(Long id) {
        Origin origin = getOne(id);
        if(origin != null){
            originDAO.delete(origin);
        }
        return origin;
    }
}
