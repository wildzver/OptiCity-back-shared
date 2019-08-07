package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import svidnytskyy.glassesspring.models.FrameColor;
import svidnytskyy.glassesspring.models.LensColor;
import svidnytskyy.glassesspring.models.Product;
//import svidnytskyy.glassesspring.models.ProductImages;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Repository
@Transactional
public interface ProductDAO extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//    @Query("select c from Product c where c.productNumber=:productNumber")
    Product getOneByProductNumber(String productNumber);

    boolean existsByProductDetailsModelNumber(int modelNumber);

    List<Product> getAllByProductDetailsModelNumber(int modelNumber);
    Product getOneByProductDetailsModelNumber(int modelNumber);

//    @Query("select p from Product p join p.productDetails pd where pd.modelNumber=:modelNumber")
//    List<Product>getAllByCategoryName(@Param("modelNumber") String modelNumber);

    @Query("select p from Product p join p.category pc where pc.name=:categoryName")
    List<Product> getAllByCategoryName(@Param("categoryName") String categoryName);

    @Query("select p from Product p join p.productDetails pd where pd.price between :minPrice and :maxPrice")
    List<Product> getAllByPrice(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);

    List<Product> getAllByLensColorInAndFrameColorIn(List<LensColor> lensColor, List<FrameColor> frameColor);

}
