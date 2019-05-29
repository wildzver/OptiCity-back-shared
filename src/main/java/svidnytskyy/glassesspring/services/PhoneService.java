package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.PhoneDAO;
import svidnytskyy.glassesspring.models.Phone;

@Service
public class PhoneService {
    private PhoneDAO phoneDAO;

    public PhoneService(PhoneDAO phoneDAO) {
        this.phoneDAO = phoneDAO;
    }

    public void save(Phone phone) {
        phoneDAO.save(phone);
    }

}
