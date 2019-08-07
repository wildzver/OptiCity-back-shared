package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import svidnytskyy.glassesspring.models.Image;

@Repository
public interface ImageDAO extends JpaRepository<Image, Long> {

    Image getByImageName(String imageName);
}
