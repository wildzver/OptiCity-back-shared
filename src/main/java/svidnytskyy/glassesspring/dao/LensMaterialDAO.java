package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.LensMaterial;

public interface LensMaterialDAO extends JpaRepository<LensMaterial, Long> {
}
