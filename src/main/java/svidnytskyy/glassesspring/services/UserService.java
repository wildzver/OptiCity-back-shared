package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.UserDAO;
import svidnytskyy.glassesspring.models.User;

import java.util.List;

@Service
public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void save(User user) {
        if (user != null){
            userDAO.save(user);
        }
    }

    public List<User> findAll() {return userDAO.findAll();}

    public List<User> findAllByFirstName(String firstName) {
        if (firstName.isEmpty()) {
            return userDAO.findAllByFirstName(firstName);
        }
        return null;
    }

    public User getOne(int id) {return userDAO.getOne(id);}

    public void update(int id) {
        User user = getOne(id);
        save(user);
    }
}
