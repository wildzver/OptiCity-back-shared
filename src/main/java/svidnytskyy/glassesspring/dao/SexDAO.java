package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.Sex;

public interface SexDAO extends JpaRepository<Sex, Long> {
}
