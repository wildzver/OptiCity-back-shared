package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.ImageDAO;
import svidnytskyy.glassesspring.dao.ProductDAO;
import svidnytskyy.glassesspring.dao.ProductDetailsDAO;
import svidnytskyy.glassesspring.repositories.BuildSearchIndex;
import svidnytskyy.glassesspring.repositories.ProductDynamicFilterRepository;
import svidnytskyy.glassesspring.repositories.ProductSearch;
import svidnytskyy.glassesspring.models.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageDAO imageDAO;

    @Autowired
    private ProductSearch productSearch;

    @Autowired
    private ProductDynamicFilterRepository productDynamicFilterRepository;

    @Autowired
    private ProductDetailsDAO productDetailsDAO;

    @Autowired
    private BuildSearchIndex buildSearchIndex;

    public void saveProductsWithoutImages(List<Product> products) {
        Iterator<Product> iter = products.iterator();
        while (iter.hasNext()) {
            Product product = iter.next();
            saveProductWithoutImages(product);
        }
    }

    public String generateProductNumber(Product product) {
        String productNumber = product.getProductDetails().getModelNumber() + "_" + product.getLensColor().getId() + "_" + product.getFrameColor().getId();
        return productNumber;
    }

    public Product saveProductWithoutImages(Product product) {

        if (product != null) {
            String productNumber = generateProductNumber(product);
            product.setProductNumber(productNumber);
            product.setUuid(UUID.randomUUID().toString());

            if (productDetailsDAO.existsByModelNumber(product.getProductDetails().getModelNumber())) {
                if (productDAO.existsByProductNumber(productNumber)) {
                    Product currentProduct = getProductByProductNumber(productNumber);
                    product.setId(currentProduct.getId());
                    product.setImages(currentProduct.getImages());
                }
                product.setProductDetails(updateProductDetails(product));
            }
            productDAO.save(product);
            buildSearchIndex.createIndexer();
        } else {
            System.out.println("PRODUCT IS NULL");
        }
        return product;
    }

    public ProductDetails updateProductDetails(Product product) {

        ProductDetails updatableProductDetails = productDetailsDAO.getProductDetailsByModelNumber(product.getProductDetails().getModelNumber());

        if (updatableProductDetails != null) {

            int newPrice = product.getProductDetails().getPrice();
            Category newCategory = product.getProductDetails().getCategory();
            int newLensWidth = product.getProductDetails().getLensWidth();
            int newLensHeight = product.getProductDetails().getLensHeight();
            LensMaterial newLensMaterial = product.getProductDetails().getLensMaterial();
            int newTotalWidth = product.getProductDetails().getTotalWidth();
            int newBracketLength = product.getProductDetails().getBracketLength();
            FrameMaterial newFrameMaterial = product.getProductDetails().getFrameMaterial();
            boolean newPolarization = product.getProductDetails().isPolarization();
            Origin newOrigin = product.getProductDetails().getOrigin();
            Sex newSex = product.getProductDetails().getSex();

            if (newPrice != 0) updatableProductDetails.setPrice(newPrice);
            if (newCategory != null) updatableProductDetails.setCategory(newCategory);
            if (newLensWidth != 0) updatableProductDetails.setLensWidth(newLensWidth);
            if (newLensHeight != 0) updatableProductDetails.setLensHeight(newLensHeight);
            if (newLensMaterial != null) updatableProductDetails.setLensMaterial(newLensMaterial);
            if (newTotalWidth != 0) updatableProductDetails.setTotalWidth(newTotalWidth);
            if (newBracketLength != 0) updatableProductDetails.setBracketLength(newBracketLength);
            if (newFrameMaterial != null) updatableProductDetails.setFrameMaterial(newFrameMaterial);
            updatableProductDetails.setPolarization(newPolarization);
            if (newOrigin != null) updatableProductDetails.setOrigin(newOrigin);
            if (newSex != null) updatableProductDetails.setSex(newSex);
            productDetailsDAO.save(updatableProductDetails);
        }
        return updatableProductDetails;
    }

    public void updateProduct(Long productId,
                              Product product) {
        if (product != null) {

            Product updatableProduct = productDAO.getOne(productId);
            if (updatableProduct != null) {
                ProductDetails updatedProductsDetails = updateProductDetails(product);
                if (updatedProductsDetails != null) product.setProductDetails(updatedProductsDetails);
                String productNumber = generateProductNumber(product);
                product.setProductNumber(productNumber);
                product.setImages(updatableProduct.getImages());
                product.setId(updatableProduct.getId());
            }
            productDAO.save(product);
        }
        buildSearchIndex.createIndexer();
    }

    public Product getProductByProductNumber(String productNumber) {
        Optional<Product> optionalProduct = Optional.ofNullable(productDAO.findFirstByProductNumber(productNumber)); // returns java8 optional
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            System.out.println("PORDUCT NUMBER DID NOT FOUND");
        }
        return null;
    }

    public Product getProductByUUID(String uuid) {
        System.out.println("MY INPUT UUID string" + uuid);
        System.out.println("MY INPUT UUID" + UUID.fromString(uuid));
        Optional<Product> optionalProduct = Optional.ofNullable(productDAO.findFirstByUuid(uuid)); // returns java8 optional
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            System.out.println("UUID DID NOT FOUND");
        }
        return null;
    }

    public List<Product> getProductsByUUIDList(String[] uuidList) {
        List<Product> products = productDAO.getProductsByUuidIn(uuidList);
        return products;
    }

    public Product saveProductImages(Long productId,
                                     MultipartFile mainImage,
                                     MultipartFile[] images) throws IOException {
        Product currentProduct = productDAO.getOne(productId);
        currentProduct = imageRoleIdentifier(currentProduct, mainImage, images);
        return productDAO.save(currentProduct);
    }

    public Product save(Product product,
                        MultipartFile mainImage,
                        MultipartFile[] images) throws IOException {
        if (product != null && mainImage != null) {
            product = imageRoleIdentifier(product, mainImage, images);
        }
        saveProductWithoutImages(product);
        return product;
    }

    public long getFreeModelNumber() {
        AtomicLong freeId = new AtomicLong(0);
        do {
            freeId.getAndIncrement();
        } while (!productDAO.getByProductDetailsModelNumber(freeId.get()).isEmpty());
        return freeId.get();
    }

    public List<Product> getProductsByProductDetailsModelNumber(long modelNumber) {
        return productDAO.findProductByProductDetailsModelNumber(modelNumber);
    }

    public Product imageRoleIdentifier(Product product,
                                       MultipartFile mainImage,
                                       MultipartFile[] images) throws IOException {
        boolean isMainImage = true;
        if (mainImage != null) product = setProductsImages(product, mainImage, isMainImage);
        if (images != null) {
            for (MultipartFile image : images) {
                product = setProductsImages(product, image, !isMainImage);
            }
        }
        return product;
    }

    public Product setProductsImages(Product product,
                                     MultipartFile image,
                                     boolean isMainImage) {
        if (image != null) {
            if (isMainImage == true) {
                Image currentMainImage = imageDAO.getProductMainImage(product.getId());
                if (currentMainImage != null) imageDAO.delete(currentMainImage);
            }

            Image currentImage = imageService.storeProductsImages(product, image, isMainImage);
            List<Image> newImageList = product.getImages();
            newImageList.add(currentImage);
            product.setImages(newImageList);
        }
        return product;
    }

    public List<Product> findAll() {
        return productDAO.findAll();
    }

    public List<Product> searchProducts(String searchParameter,
                                        Integer numberOfResults,
                                        String sexes,
                                        Integer minPrice,
                                        Integer maxPrice,
                                        String lensColors,
                                        String frameColors,
                                        String frameMaterials,
                                        String diopters,
                                        boolean polarization) throws
            InterruptedException {
        List<Product> result = productSearch.searchNumberProducts(
                searchParameter,
                numberOfResults,
                sexes,
                minPrice,
                maxPrice,
                lensColors,
                frameColors,
                frameMaterials,
                diopters,
                polarization);
        return result;
    }

    public Page<Product> searchPagedProducts(String searchParameter,
                                             Integer pageNumber,
                                             Integer pageSize,
                                             String sortBy,
                                             String sortDirection,
                                             List<Sex> sex,
                                             Integer minPrice,
                                             Integer maxPrice,
                                             List<LensColor> lensColor,
                                             List<FrameColor> frameColor,
                                             List<FrameMaterial> frameMaterial,
                                             List<Diopter> diopter,
                                             boolean polarization) throws InterruptedException {
        StringBuilder sexes = new StringBuilder();
        sex.forEach(item -> sexes.append(item.getName().concat(" ")));
        StringBuilder lensColors = new StringBuilder();
        if (lensColor != null) lensColor.forEach(color -> lensColors.append(color.getName().concat(" ")));
        StringBuilder frameColors = new StringBuilder();
        if (frameColor != null) frameColor.forEach(color -> frameColors.append(color.getName().concat(" ")));
        StringBuilder frameMaterials = new StringBuilder();
        if (frameMaterial != null) frameMaterial.forEach(item -> frameMaterials.append(item.getName().concat(" ")));
        StringBuilder diopters = new StringBuilder();
        if (diopter != null) diopter.forEach(item -> diopters.append(String.valueOf(item.getValue()).concat(" ")));
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> result = productSearch.searchAllProducts(
                searchParameter,
                pageable,
                sortBy,
                sortDirection,
                sexes.toString(),
                minPrice,
                maxPrice,
                lensColors.toString(),
                frameColors.toString(),
                frameMaterials.toString(),
                diopters.toString(),
                polarization);
        return result;
    }

    public int getMinPrice() {
        return productDetailsDAO.getMinPrice();
    }

    public int getMaxPrice() {
        return productDetailsDAO.getMaxPrice();
    }

    public Page<Product> findProductsWithPicturesByCriteria(
            String category,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDirection,
            List<Sex> sex,
            Integer minPrice,
            Integer maxPrice,
            List<LensColor> lensColor,
            List<FrameColor> frameColor,
            List<FrameMaterial> frameMaterial,
            List<Diopter> diopter,
            boolean polarization) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productDynamicFilterRepository.filterProductsByCriteria(
                category,
                sortBy,
                sortDirection,
                sex,
                minPrice,
                maxPrice,
                lensColor,
                frameColor,
                frameMaterial,
                diopter,
                polarization,
                pageable);
        return products;
    }

    public List<Product> findProductsByImagesIsNull() {
        return productDAO.findAllByImagesIsNull();
    }

    public Product getOne(long id) {
        return productDAO.getOne(id);
    }

    public List<Product> getAllByModelNumberThroughProductNumber(String productNumber) {
        long modelNumber = Integer.parseInt(productNumber.split("_")[0]);
        System.out.println("findDistinctByProductDetailsModelNumberAndImagesIsNotNull!!!!" + productDAO.findDistinctByProductDetailsModelNumberAndImagesIsNotNull(modelNumber));
        return productDAO.findDistinctByProductDetailsModelNumberAndImagesIsNotNull(modelNumber);
    }

    public Product getProductById(long id) {
        return productDAO.getOne(id);
    }

    public List<Image> getProductImages(long id) {
        return imageDAO.getProductImages(id);
    }


    public void delete(long id) {
        Product product = productDAO.getOne(id);
        if (product != null) {
            productDAO.deleteById(id);
            if (product.getProductDetails().getProducts().isEmpty()) {
                productDetailsDAO.delete(product.getProductDetails());
            }
        }
    }
}
