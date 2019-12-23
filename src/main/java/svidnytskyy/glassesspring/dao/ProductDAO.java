package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import svidnytskyy.glassesspring.interfaces.ProductDynamicFilter;
import svidnytskyy.glassesspring.models.Image;
import svidnytskyy.glassesspring.models.Product;
//import svidnytskyy.glassesspring.models.ProductImages;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface ProductDAO extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Product findByProductNumber(String productNumber);

//    @Query("select p from Product p where p.productNumber=:productNumber")
    Product getOneByProductNumber(/*@Param("productNumber") */String productNumber);
    Product getOneByUuid(/*@Param("productNumber") */String uuid);

    List<Product> getProductsByUuidIn(String[] uuidList);

    List<Product> getByProductDetailsModelNumber(long modelNumber);

    boolean existsByUuid(String uuid);

    Product findById(long id);



    boolean existsByProductNumber(String productNumber);

    List<Product> findDistinctByProductDetailsModelNumberAndImagesIsNotNull(long modelNumber);

    @Query("select count(p) from Product p join p.productDetails pd where pd.modelNumber=:modelNumber")
    Product getProductByProductDetailsModelNumber(@Param("modelNumber") long modelNumber);

    Product findFirstByProductDetailsModelNumber(long modelNumber);

    Product findFirstByProductNumber(String productNumber);

    Product findFirstByUuid(String uuid);



    List<Product> findProductByProductDetailsModelNumber(long modelNumber);

    List<Product> findAllByImagesIsNull();

//    List<Product> findAllByCategoryUaName(String searchParameter);



//    List<Product> findAllByImagesIsNotNull();

//    @Query("select p from Product p join p.productDetails pd where pd.modelNumber=:modelNumber")
//    List<Product>getAllByCategoryName(@Param("modelNumber") String modelNumber);

//    @Query("select distinct p from Product p left join p.images pi join p.category pc where pi.id is not null and pc.name=:categoryName")
//    List<Product> getAllByCategoryName(@Param("categoryName") String categoryName);

    @Query("select p from Product p join p.productDetails pd where pd.price between :minPrice and :maxPrice")
    List<Product> getAllByPrice(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);

//    List<Product> getAllByCategory(List<LensColor> lensColor, List<FrameColor> frameColor);

    List<Product> findAllByImagesIsNotNull(/*List<Product> products*/);

}
