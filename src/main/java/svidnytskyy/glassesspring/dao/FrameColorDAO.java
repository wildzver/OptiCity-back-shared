package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.FrameColor;

public interface FrameColorDAO extends JpaRepository<FrameColor, Long> {
    FrameColor getFrameColorById(long id);
}
