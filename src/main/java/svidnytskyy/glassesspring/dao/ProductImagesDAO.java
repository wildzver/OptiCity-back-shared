//package svidnytskyy.glassesspring.dao;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//import svidnytskyy.glassesspring.models.ProductImages;
//
//import java.util.List;
//
//@Repository
//public interface ProductImagesDAO extends JpaRepository<ProductImages, Long> {
//
//    @Query("select distinct im from Image im inner join ProductImages pr_im on pr_im.image.id = im.id inner join Product pr on pr.id = pr_im.product.id")
////    @Query("select distinct pr, im from Product pr inner join ProductImages pr_im on pr_im.product.id = pr.id inner join Image im on im.id = pr_im.image.id")
//    List<ProductImages> findAllWithImages();
//}
