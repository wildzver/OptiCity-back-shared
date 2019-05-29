package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.Phone;

public interface PhoneDAO extends JpaRepository<Phone, Integer> {
}
