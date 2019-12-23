package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import svidnytskyy.glassesspring.models.Image;

import java.util.List;

@Repository
public interface ImageDAO extends JpaRepository<Image, Long> {

    Image getByImageName(String imageName);

    @Query("select p.images from Product p where p.id =:id")
    List<Image> getProductImages(@Param("id") long id);

    @Query("select i from Product p join p.images i where p.id =:id and i.isMainImage = true ")
    Image getProductMainImage(@Param("id") long id);

}
