package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.User;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {
    List<User> findAllByFirstName(String firstName);

}
