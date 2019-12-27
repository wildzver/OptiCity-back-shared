package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import svidnytskyy.glassesspring.models.Product;
import java.util.List;

@Repository
@Transactional
public interface ProductDAO extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsByProductNumber(String productNumber);

    Product getOneByUuid(String uuid);

    List<Product> getProductsByUuidIn(String[] uuidList);

    List<Product> getByProductDetailsModelNumber(long modelNumber);

    List<Product> findDistinctByProductDetailsModelNumberAndImagesIsNotNull(long modelNumber);

    Product findFirstByProductDetailsModelNumber(long modelNumber);

    Product findFirstByProductNumber(String productNumber);

    Product findFirstByUuid(String uuid);

    List<Product> findProductByProductDetailsModelNumber(long modelNumber);

    List<Product> findAllByImagesIsNull();
}
