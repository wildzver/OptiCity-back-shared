package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.Origin;

public interface OriginDAO extends JpaRepository<Origin, Long> {
}
