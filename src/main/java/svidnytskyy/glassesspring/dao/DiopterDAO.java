package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.Diopter;

public interface DiopterDAO extends JpaRepository<Diopter, Long> {
}
