package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.FrameMaterial;

public interface FrameMaterialDAO extends JpaRepository<FrameMaterial, Long> {
}
