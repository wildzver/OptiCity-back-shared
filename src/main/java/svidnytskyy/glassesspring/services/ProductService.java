package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.ProductDAO;
//import svidnytskyy.glassesspring.dao.ProductDAO2;
import svidnytskyy.glassesspring.models.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Autowired
    private ImageService imageService;


    public Product save(Product product,
                        MultipartFile mainImage,
                        MultipartFile[] images) throws IOException {
        if (product != null) {
            boolean isMainImage = true;
            setProductsImages(product, mainImage, isMainImage);
            for (MultipartFile image : images) {
                setProductsImages(product, image, !isMainImage);
            }
            if (productDAO.existsByProductDetailsModelNumber(product.getProductDetails().getModelNumber())) {
                Product currentProduct = productDAO.getOneByProductDetailsModelNumber(product.getProductDetails().getModelNumber());
                product.setProductDetails(currentProduct.getProductDetails());
            }
            productDAO.save(product);
        }
        return product;
    }

    public void setProductsImages(Product product,
                                  MultipartFile image,
                                  boolean isMainImage) throws IOException {
        imageService.storeImage(image, isMainImage);
        Image currentImage = imageService.getImageByName(image.getOriginalFilename());
        System.out.println("getOriginalFilename!!!".toUpperCase());
        System.out.println(currentImage);
        product.getImages().add(currentImage);
    }

    public List<Product> findAll() {
        return productDAO.findAll(new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
            }
        });
    }


    public List<Product> findByCriteria(Integer minPrice, Integer maxPrice, List<LensColor> lensColor, List<FrameColor> frameColor) {
        return productDAO.findAll(new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Product, ProductDetails> productDetailsJoin = root.join(Product_.productDetails);
                List<Predicate> predicates = new ArrayList<>();
//                final Join<Product, ProductDetails> productDetailsJoin = root.join(Product.productDetails, JoinType.LEFT);
//                EntityManagerFactory emf = Persistence.createEntityManagerFactory("productsData");
//                EntityManager em = emf.createEntityManager();
//                EntityTransaction tx = em.getTransaction();
//                tx.begin();
//                CriteriaQuery<Product> cq = criteriaBuilder.createQuery(Product.class);
//                Metamodel m = em.getMetamodel();
//                EntityType<Product> Product_ = m.entity(Product.class);
//                Root<Product> productRoot = cq.from(Product.class);
//                Join<Product, ProductDetails> productDetails = productRoot.join(Product_.)
                if (!CollectionUtils.isEmpty(lensColor)) {
                    predicates.add(criteriaBuilder.and(root.get("lensColor").in(lensColor)));
                }
                if (!CollectionUtils.isEmpty(frameColor)) {
                    predicates.add(criteriaBuilder.and(root.get("frameColor").in(frameColor)));
//                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("frameColor"), frameColor)));
                }
                if (minPrice != null && maxPrice != null) {
                    predicates.add(criteriaBuilder.between(productDetailsJoin.get(ProductDetails_.price), minPrice, maxPrice));
                }
                System.out.println("CRITERIA BUILDER!!!");
                System.out.println(criteriaBuilder);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

//    public List<Product> findByCriteria(int minPrice, int maxPrice, LensColor lensColor, FrameColor frameColor){
//        return productDAO.findAll(new Specification<Product>() {
//            @Override
//            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicates = new ArrayList<>();
//                Join<Product, ProductDetails> productDetailsListJoin = root.join(Product_.productDetails);
//                if(lensColor!=null) {
//                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("lensColor"), lensColor)));
//                }
//                if(frameColor!=null){
//                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("frameColor"), frameColor)));
//                }
//
//                if(minPrice!=0 && maxPrice!=0) {
//                    predicates.add(criteriaBuilder.between(productDetailsListJoin.get(ProductDetails_.price), minPrice, maxPrice));
//                }
//                System.out.println("CRITERIA BUILDER!!!");
//                System.out.println(criteriaBuilder);
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        });
//    }

    public List<Product> findAllByLensColor(List<LensColor> lensColor, List<FrameColor> frameColor) {
        return productDAO.getAllByLensColorInAndFrameColorIn(lensColor, frameColor);
    }

    public Product getOne(long id) {
        return productDAO.getOne(id);
    }

    public Product getProductByProductNumber(String productNumber) {
        return productDAO.getOneByProductNumber(productNumber);
    }

    public List<Product> getAllByModelNumberThroughProductNumber(String productNumber) {
        int modelNumber = Integer.parseInt(productNumber.split("_")[0]);
        return productDAO.getAllByProductDetailsModelNumber(modelNumber);
    };

    public Product getOneByProductNumber(String productNumber) {
        return productDAO.getOneByProductNumber(productNumber);
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productDAO.getAllByCategoryName(categoryName);
    }

//    public void update(long id) {
//        Product product = getOne(id);
//        save(product);
//    }

    public Product delete(long id) {
        Product product = getOne(id);
        if (product != null) {
            productDAO.delete(product);
        }
        return product;
    }

}
