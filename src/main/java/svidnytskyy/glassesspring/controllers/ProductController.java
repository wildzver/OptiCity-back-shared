package svidnytskyy.glassesspring.controllers;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import svidnytskyy.glassesspring.models.*;
import svidnytskyy.glassesspring.services.ImageService;
import svidnytskyy.glassesspring.services.ProductService;
import svidnytskyy.glassesspring.services.ProductsExcelFileService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping({"/api/products"})
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ImageService imageService;

    @Autowired
    ProductsExcelFileService productsExcelFileService;

    @PostMapping("/addProduct")
    public Product addProduct(@RequestPart Product product,
                              @Valid @RequestParam(name = "mainImage", required = false) MultipartFile mainImage,
                              @RequestParam(name = "image", required = false) MultipartFile[] images,
                              BindingResult result,
                              RedirectAttributes redirectMap) throws IOException {
        return productService.save(product, mainImage, images);
    }

    @PostMapping("/{productId}/update")
    public void updateProduct(
            @PathVariable Long productId,
            @RequestPart Product product) {
        productService.updateProduct(productId, product);
    }

    @GetMapping("/addProduct/getFreeModelNumber")
    public long getFreeModelNumber() {
        return productService.getFreeModelNumber();
    }

    @GetMapping("/modelNumbers/{modelNumber}")
    public List<Product> getProductsByProductDetailsModelNumber(@PathVariable long modelNumber) {
        return productService.getProductsByProductDetailsModelNumber(modelNumber);
    }

    @PostMapping("/uploadProducts")
    public List<Product> uploadProductsExcelFile(@RequestParam(name = "excelProducts") MultipartFile file, Model model) {
        try {
            productsExcelFileService.store(file);
            model.addAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Fail! -> uploaded filename: " + file.getOriginalFilename());
        }
        return productService.findProductsByImagesIsNull();
    }

    @PostMapping("/{productId}/uploadProductImages")
    public void uploadProductImages(
            @PathVariable Long productId,
            @RequestParam(name = "mainImage", required = false) MultipartFile mainImage,
            @RequestParam(name = "image", required = false) MultipartFile[] images) throws IOException {
        productService.saveProductImages(productId, mainImage, images);
    }

    @GetMapping("/{productId}/images/{imageId}/delete")
    public List<Image> deleteProductImage(
            @PathVariable long productId,
            @PathVariable long imageId) {
        imageService.deleteProductImage(imageId);
        return productService.getProductImages(productId);
    }

    @GetMapping("{productId}/images")
    public List<Image> getProductImages(
            @PathVariable long productId) {
        return productService.getProductImages(productId);
    }

    @GetMapping("/productsWithoutImages")
    public List<Product> getProductsWithoutImages() {
        return productService.findProductsByImagesIsNull();
    }

    @GetMapping("/downloadProducts")
    public ResponseEntity<InputStreamResource> downloadProductsExcelFile() {
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

    @GetMapping("/cart/{uuid}")
    public Product getProductByUUID(@PathVariable String uuid) {
        return productService.getProductByUUID(uuid);
    }

    @PostMapping("/UUIDList")
    public List<Product> getProductsByUUIDList(@RequestPart String[] uuidList) {
        List<Product> products = productService.getProductsByUUIDList(uuidList);
        return products;
    }

    @GetMapping
    public Page<Product> findProductsWithPicturesByCriteria(
            @PathVariable(value = "category", required = false) String category,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "1000") Integer pageSize,
            @RequestParam(required = false, defaultValue = "productNumber") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) List<Sex> sex,
            @RequestParam(required = false, defaultValue = "0") Integer minPrice,
            @RequestParam(required = false, defaultValue = "100000") Integer maxPrice,
            @RequestParam(required = false) List<LensColor> lensColor,
            @RequestParam(required = false) List<FrameColor> frameColor,
            @RequestParam(required = false) List<FrameMaterial> frameMaterial,
            @RequestParam(required = false) List<Diopter> diopter,
            @RequestParam(required = false) boolean polarization
    ) {
        return productService.findProductsWithPicturesByCriteria(
                category,
                pageNumber,
                pageSize,
                sortBy,
                sortDirection,
                sex,
                minPrice,
                maxPrice,
                lensColor,
                frameColor,
                frameMaterial,
                diopter,
                polarization);
    }

    @GetMapping("/{category}")
    public Page<Product> getProductsByCategory(
            @PathVariable("category") String category,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(required = false, defaultValue = "productNumber") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) List<Sex> sex,
            @RequestParam(required = false, defaultValue = "0") Integer minPrice,
            @RequestParam(required = false, defaultValue = "100000") Integer maxPrice,
            @RequestParam(required = false) List<LensColor> lensColor,
            @RequestParam(required = false) List<FrameColor> frameColor,
            @RequestParam(required = false) List<FrameMaterial> frameMaterial,
            @RequestParam(required = false) List<Diopter> diopter,
            @RequestParam(required = false) boolean polarization
    ) {
        return productService.findProductsWithPicturesByCriteria(
                category,
                pageNumber,
                pageSize,
                sortBy,
                sortDirection,
                sex,
                minPrice,
                maxPrice,
                lensColor,
                frameColor,
                frameMaterial,
                diopter,
                polarization);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String searchParameter,
            @RequestParam(required = false) Integer numberOfResults,
            @RequestParam(required = false, defaultValue = "") String sex,
            @RequestParam(required = false, defaultValue = "0") Integer minPrice,
            @RequestParam(required = false, defaultValue = "100000") Integer maxPrice,
            @RequestParam(required = false, defaultValue = "") String lensColor,
            @RequestParam(required = false, defaultValue = "") String frameColor,
            @RequestParam(required = false, defaultValue = "") String frameMaterial,
            @RequestParam(required = false, defaultValue = "") String diopter,
            @RequestParam(required = false) boolean polarization
    ) throws InterruptedException, ParseException {
        List<Product> products = productService.searchProducts(
                searchParameter,
                numberOfResults,
                sex,
                minPrice,
                maxPrice,
                lensColor,
                frameColor,
                frameMaterial,
                diopter,
                polarization
        );
        return products;
    }

    @GetMapping("/searchPaged")
    public Page<Product> searchPagedProducts(
            @RequestParam(required = false) String searchParameter,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "16") Integer pageSize,
            @RequestParam(required = false, defaultValue = "productNumber") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) List<Sex> sex,
            @RequestParam(required = false, defaultValue = "0") Integer minPrice,
            @RequestParam(required = false, defaultValue = "100000") Integer maxPrice,
            @RequestParam(required = false) List<LensColor> lensColor,
            @RequestParam(required = false) List<FrameColor> frameColor,
            @RequestParam(required = false) List<FrameMaterial> frameMaterial,
            @RequestParam(required = false) List<Diopter> diopter,
            @RequestParam(required = false) boolean polarization

    ) throws InterruptedException {
        Page<Product> products = productService.searchPagedProducts(
                searchParameter,
                pageNumber,
                pageSize,
                sortBy,
                sortDirection,
                sex,
                minPrice,
                maxPrice,
                lensColor,
                frameColor,
                frameMaterial,
                diopter,
                polarization);
        return products;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();
        String fieldName = ex.getName();
        String errorMessage = "typeMismatchException";
        errors.put(fieldName, errorMessage);
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getMinPrice")
    public int getMinPrice() {
        return productService.getMinPrice();
    }

    @GetMapping("/getMaxPrice")
    public int getMaxPrice() {
        return productService.getMaxPrice();
    }

    @GetMapping("/admin/{id}")
    public Product getProductById(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping(path = {"/{id}/delete"})
    public void delete(@PathVariable("id") long id) {
        productService.delete(id);
    }
}
