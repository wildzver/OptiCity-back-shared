package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.LensColor;

public interface LensColorDAO extends JpaRepository<LensColor, Long> {
}
