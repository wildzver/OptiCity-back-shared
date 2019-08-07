//package svidnytskyy.glassesspring.services;
//
//import org.springframework.stereotype.Service;
//import svidnytskyy.glassesspring.dao.ProductImagesDAO;
//import svidnytskyy.glassesspring.models.ProductImages;
//
//import java.util.List;
//
//@Service
//public class ProductImagesService {
//    private ProductImagesDAO productImagesDAO;
//
//    public ProductImagesService(ProductImagesDAO productImagesDAO) {
//        this.productImagesDAO = productImagesDAO;
//    }
//
//    public ProductImages save(ProductImages productImages) {
//        if (productImages != null) {
//            productImagesDAO.save(productImages);
//        }
//        return productImages;
//    }
//
//    public List<ProductImages> findAllWithImages() {
//        return productImagesDAO.findAllWithImages();
//    }
//
//    public List<ProductImages> findAll() {
//        return productImagesDAO.findAll();
//    }
//}
