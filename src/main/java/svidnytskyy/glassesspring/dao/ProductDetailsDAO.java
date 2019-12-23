package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import svidnytskyy.glassesspring.models.ProductDetails;

@Repository
public interface ProductDetailsDAO extends JpaRepository<ProductDetails, Long> {
    ProductDetails getProductDetailsByModelNumber(long modelNumber);

    boolean existsByModelNumber(long modelNumber);

    @Query("select min(pd.price)from ProductDetails pd")
    int getMinPrice();

    @Query("select max(pd.price)from ProductDetails pd")
    int getMaxPrice();

}
