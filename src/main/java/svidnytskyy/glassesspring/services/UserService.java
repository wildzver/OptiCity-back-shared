package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.UserDAO;
import svidnytskyy.glassesspring.models.Product;
import svidnytskyy.glassesspring.models.User;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public PasswordEncoder passwordEncoder;

    public User save(User user) {
        if (user != null) {
            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userDAO.save(user);
        }
        return user;
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public List<User> findAllByFirstName(String firstName) {
        if (firstName.isEmpty()) {
            return userDAO.findAllByFirstName(firstName);
        }
        return null;
    }

    public User getOne(long id) {
        return userDAO.getOne(id);
    }

    public void update(int id) {
        User user = getOne(id);
        save(user);
    }

    public User delete(int id) {
        User user = getOne(id);
        if (user != null) {
            userDAO.delete(user);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("FOUND!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(userDAO.getOneByEmail(email));
        return userDAO.getOneByEmail(email);

    }
}
