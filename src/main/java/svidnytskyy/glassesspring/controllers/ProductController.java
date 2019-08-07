package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.models.*;
import svidnytskyy.glassesspring.services.ImageService;
import svidnytskyy.glassesspring.services.ProductService;
import svidnytskyy.glassesspring.services.ProductsExcelFileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"/products"})
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ImageService imageService;
    @Autowired
    ProductsExcelFileService productsExcelFileService;
//    @Autowired
//    ProductImagesService productImagesService;
//    List<String> allImages = new ArrayList<>();

    @PostMapping("/addProduct")
    public Product addProduct(@RequestPart Product product,
                              @RequestParam(name = "mainImage", required = false) MultipartFile mainImage,
                              @RequestParam(name = "image", required = false) MultipartFile[] images) throws IOException {
        System.out.println(images.toString());
        System.out.println(product);
        String productNumber = product.getProductDetails().getModelNumber() + "_" + product.getLensColor().getId() + "_" + product.getFrameColor().getId();
        product.setProductNumber(productNumber);

//        ProductDetails productDetails = product.getProductDetails();
//        productDetails.setProducts(Collections.singleton(product));
        productService.save(product, mainImage, images);


//        for(MultipartFile image: images) {
//            imageService.storeImage(image);
//            allImages.add(image.getOriginalFilename());
//            Image currentImage = imageService.getImageByName(image.getOriginalFilename());
////        product.setImage(currentImage);
//            System.out.println("getOriginalFilename!!!".toUpperCase());
//            System.out.println(image.getOriginalFilename());
//            Product currentProduct = productService.getOneByProductNumber(product.getProductNumber());
//            ProductImages productImages = new ProductImages();
//            productImages.setProduct(currentProduct);
//            productImages.setImage(currentImage);
//            productImages.setMainImage(false);
//            productImagesService.save(productImages);
//        }

        return product;
    }

    @PostMapping("/uploadProducts")
    public Model uploadProductsExcelFile(@RequestParam("uploadfile") MultipartFile file, Model model) {
            try {
                productsExcelFileService.store(file);
                model.addAttribute("message", "File uploaded successfully!");
            } catch (Exception e) {
                model.addAttribute("message", "Fail! -> uploaded filename: " + file.getOriginalFilename());
            }
        return model;
    }

    @GetMapping("/downloadProducts")
    public ResponseEntity<InputStreamResource> downloadProductsExcelFile(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(productsExcelFileService.loadFile()));
    }


    @GetMapping("/{category}/{productNumber}")
    public List<Product> getProductsByModelNumberThroughProductNumber(@PathVariable("productNumber") String productNumber) {

        return productService.getAllByModelNumberThroughProductNumber(productNumber);
    }
    
    @GetMapping("/cart/{productNumber}")
    public Product getProductByProductNumber(@PathVariable("productNumber") String productNumber){
        return productService.getProductByProductNumber(productNumber);
    }

    @GetMapping("/{category}")
    public List<Product> getProductsByCategory(@PathVariable("category") String category) {
        System.out.println("GET CATEGORY!!!!");
        System.out.println(category);
        return productService.getProductsByCategory(category);
    }

//    @GetMapping
//    public List<Product> findAll() {
//        System.out.println("GET ALL PRODUCTS!!!!!!!!!!");
//        return productService.findAll();
//    }

    @GetMapping
    public List<Product> findAll(
            @RequestParam(required = false, defaultValue = "0") Integer minPrice,
            @RequestParam(required = false, defaultValue = "100000") Integer maxPrice,
            @RequestParam(required = false) List<LensColor> lensColor,
            @RequestParam(required = false) List<FrameColor> frameColor
    ) {
        System.out.println(lensColor);
        System.out.println("GET!!!!!!!!!!");
        return productService.findByCriteria(minPrice, maxPrice, lensColor, frameColor);
    }

    @GetMapping(params = "")

//    @GetMapping
//    public List<ProductImages> findAll(){
//        return productImagesService.findAllWithImages();
//    }
//

    @DeleteMapping(path = {"/{id}"})
    public Product delete(@PathVariable("id") long id) {
        return productService.delete(id);
    }

}
