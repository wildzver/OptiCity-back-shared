package svidnytskyy.glassesspring.services;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.ImageDAO;
import svidnytskyy.glassesspring.dao.ProductDAO;
//import svidnytskyy.glassesspring.dao.ProductDAO2;
import svidnytskyy.glassesspring.dao.ProductDetailsDAO;
import svidnytskyy.glassesspring.repositories.BuildSearchIndex;
import svidnytskyy.glassesspring.repositories.ProductDynamicFilterRepository;
import svidnytskyy.glassesspring.repositories.ProductSearch;
import svidnytskyy.glassesspring.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @PersistenceContext
    EntityManager em;

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

    public static <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public void saveProductsWithoutImages(List<Product> products) {
        Iterator<Product> iter = products.iterator();
        while (iter.hasNext()) {
            Product product = iter.next();
            saveProductWithoutImages(product);
//            if (product != null) {
//                Product currentProduct = getProductByProductDetailsModelNumber(product.getProductDetails().getModelNumber());
//                String productNumber = product.getProductDetails().getModelNumber() + "_" + product.getLensColor().getId() + "_" + product.getFrameColor().getId();
//                product.setProductNumber(productNumber);
//
//                if (productDetailsDAO.existsByModelNumber(product.getProductDetails().getModelNumber())) {
//                    if (productDAO.existsByProductNumber(productNumber)) {
//                        product.setId(getProductByProductNumber(productNumber).getId());
//                        product.setImages(getProductByProductNumber(productNumber).getImages());
//                    }
//                    ProductDetails updatableProductDetails = productDetailsDAO.getProductDetailsByModelNumber(product.getProductDetails().getModelNumber());
//
//                    int newPrice = product.getProductDetails().getPrice();
//                    int newLensWidth = product.getProductDetails().getLensWidth();
//                    int newLensHeight = product.getProductDetails().getLensHeight();
//                    String newLensMaterial = product.getProductDetails().getLensMaterial();
//                    int newTotalWidth = product.getProductDetails().getTotalWidth();
//                    int newBracketLength = product.getProductDetails().getBracketLength();
//                    String newFrameMaterial = product.getProductDetails().getFrameMaterial();
//                    String newOrigin = product.getProductDetails().getOrigin();
//
//                    if (newPrice != 0) updatableProductDetails.setPrice(newPrice);
//                    if (newLensWidth != 0) updatableProductDetails.setLensWidth(newLensWidth);
//                    if (newLensHeight != 0) updatableProductDetails.setLensHeight(newLensHeight);
//                    if (newLensMaterial != null) updatableProductDetails.setLensMaterial(newLensMaterial);
//                    if (newTotalWidth != 0) updatableProductDetails.setTotalWidth(newTotalWidth);
//                    if (newBracketLength != 0) updatableProductDetails.setBracketLength(newBracketLength);
//                    if (newFrameMaterial != null) updatableProductDetails.setFrameMaterial(newFrameMaterial);
//                    if (newOrigin != null) updatableProductDetails.setOrigin(newOrigin);
//
//                    productDetailsDAO.save(updatableProductDetails);
//                    product.setProductDetails(updatableProductDetails);
//                }
//
//                productDAO.save(product);
//
//            } else {
//                System.out.println("PRODUCT IS NULL");
//            }
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

//            UUID uuid = generateUUID();
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
            System.out.println("NEW POLARIZATION FOR CHECK" + newPolarization);
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
//                updatableProduct = product;
            }
            productDAO.save(product);
        }
        buildSearchIndex.createIndexer();
    }


    public Product getProductByProductDetailsModelNumber(long modelNumber) {
        Optional<Product> optionalProduct = Optional.ofNullable(productDAO.findFirstByProductDetailsModelNumber(modelNumber)); // returns java8 optional
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            System.out.println("MODEL NUMBER DID NOT FOUND");
        }
        return null;
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
        System.out.println("uuidList>>" + uuidList);

        List<Product> products = productDAO.getProductsByUuidIn(uuidList);
        System.out.println("productList>>" + products);
        return products;
    }

    public Product saveProductImages(Long productId,
                                     MultipartFile mainImage,
                                     MultipartFile[] images) throws IOException {
//        Product currentProduct = productDAO.getOneByProductNumber(productNumber);
        Product currentProduct = productDAO.getOne(productId);
        System.out.println("MY CURRENT PRODUCT!" + currentProduct);
        System.out.println("MY CURRENT PRODUCTDETAILS!" + currentProduct.getProductDetails());
        currentProduct = imageRoleIdentifier(currentProduct, mainImage, images);
        return productDAO.save(currentProduct);
    }

//    public Product deleteProductImage(Long id,
//                              String imageName) {
//        Product currentProduct = productDAO.getOne(id);
//        List<Image> newImageList = currentProduct.getImages();
//        newImageList.remove(1);
//        currentProduct.setImages(newImageList);
//        productDAO.save(currentProduct);
//        return currentProduct;
//    }

    public Product save(Product product,
                        MultipartFile mainImage,
                        MultipartFile[] images) throws IOException {

        if (product != null && mainImage != null) {
//            String productNumber = product.getProductDetails().getModelNumber() + "_" + product.getLensColor().getId() + "_" + product.getFrameColor().getId();
//            product.setProductNumber(productNumber);

            product = imageRoleIdentifier(product, mainImage, images);

//            if (productDetailsDAO.existsByModelNumber(product.getProductDetails().getModelNumber())) {
//                Product currentProduct = getProductByProductDetailsModelNumber(product.getProductDetails().getModelNumber());
//                System.out.println("MY CURRENT PRODUCT" + currentProduct);
//
////                product.setProductDetails(currentProduct.getProductDetails());
//                product.setProductDetails(productDetailsDAO.getProductDetailsByModelNumber(product.getProductDetails().getModelNumber()));
//            }
//            productDAO.save(product);
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
//        System.out.println("?? product:" + product + "mainIma: " + mainImage.getOriginalFilename() + "images: " + images);
        if (mainImage != null) product = setProductsImages(product, mainImage, isMainImage);
        if (images != null) {
            for (MultipartFile image : images) {
                product = setProductsImages(product, image, !isMainImage);
            }
        }
        System.out.println("MY PRODUCT WITH NEW IMAGES" + product.getImages());
        return product;
    }

    public Product setProductsImages(Product product,
                                     MultipartFile image,
                                     boolean isMainImage) throws IOException {
        if (image != null) {
            if (isMainImage == true) {
                Image currentMainImage = imageDAO.getProductMainImage(product.getId());
                System.out.println("currentMainImage>>" + currentMainImage);
                if (currentMainImage != null) imageDAO.delete(currentMainImage);
            }

            Image currentImage = imageService.storeProductsImages(product, image, isMainImage);
            System.out.println("getOriginalFilename!!!".toUpperCase());
            System.out.println(currentImage);
            List<Image> newImageList = product.getImages();
            newImageList.add(currentImage);
            product.setImages(newImageList);
//        currentImage.setProduct(product);
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
            InterruptedException, ParseException {

//        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Product2");
//        EntityManager em = factory.createEntityManager();
//        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
//        em.getTransaction().begin();
//        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
//                .forEntity(Product.class).get();
//        org.apache.lucene.search.Query luceneQuery = qb
//                .keyword()
//                .onFields("productNumber")
//                .matching(searchParameter)
//                .createQuery();
//        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);
//        List<Product> result = jpaQuery.getResultList();
//        em.getTransaction().commit();
//        em.close();
//        System.out.println("MY QUERY" + jpaQuery.toString());
//        System.out.println("MY RESULT" + result.toString());

//        productDAO.getQuery(searchParameter);
        long start = System.currentTimeMillis();
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
        long end = System.currentTimeMillis();
        System.out.println("ProductService execution time: " + (end - start));
        System.out.println("MY ABSOLUTRE RESULT!" + result);
//        return productDAO.findAllByCategoryUaName(searchParameter);
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
//        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, sortBy));

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

//        Sort.Direction direction;
//        if (sortDirection.equalsIgnoreCase("desc")) {
//            direction = Sort.Direction.DESC;
//        } else {
//            direction = Sort.Direction.ASC;
//        }
//        System.out.println("MY DIRECTION" + direction);
//        Sort sort = Sort.by(new Sort.Order(direction, "productNumber"));
//        List<Product> pr = new LinkedList<>();
        String minPriceString = minPrice.toString();
//        Pattern pattern = Pattern.compile("^[0-9]+$");
//        Matcher matcher = pattern.matcher(minPriceString);
//        int minPriceInt;
//        int maxPriceInt;
//        if (matcher.matches()) {
//            minPriceInt = Integer.parseInt(minPrice);
//            System.out.println("MY MIN PRICE" + minPriceInt);
//        } else {
//            minPriceInt = Integer.parseInt(minPrice);
//            minPriceInt = productDetailsDAO.getMinPrice();
//            System.out.println("MY MIN PRICE2" + minPriceInt);
//        }
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
        System.out.println("minPriceString" + minPriceString);
//        System.out.println("pattern" + pattern);
        System.out.println("minPrice" + minPrice);

//        Page<Product> products = productDAO.findAll(
//                runCriteriaBuilder(category, sortBy, sortDirection, minPrice, maxPrice, lensColor, frameColor),
//                pageable);
//        List<Product> distinctProducts = pr.stream().filter(distinctByKey(Product::getProductNumber)).collect(Collectors.toList());
//        int distinctProductsSize = (int)pr.stream().filter(distinctByKey(Product::getProductNumber)).count();
//        int start = (int)pageable.getOffset();
//        int end = (start + pageable.getPageSize()) > distinctProductsSize ? distinctProductsSize : (start + pageable.getPageSize());
//        List<Product> sublist = new LinkedList<Product>().subList(start, end);
//        return new PageImpl<Product>(pr.subList(start, end), pageable, distinctProductsSize);
//        return new PageImpl<Product>(products, pageable, products.size());
        return products;
    }

//    public Specification<Product> runCriteriaBuilder(
//            String category,
//            String sortBy,
//            String sortDirection,
//            Integer minPrice,
//            Integer maxPrice,
//            List<LensColor> lensColor,
//            List<FrameColor> frameColor
//    ) {
//        return new Specification<Product>() {
//
//
//            @Override
//            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                query.distinct(true);
//
////                Join<Product, ProductDetails> productDetailsJoin = root.join(Product_.productDetails, JoinType.LEFT);
//                Join<Product, ProductDetails> productDetailsJoin = (Join<Product, ProductDetails>) root.fetch(Product_.productDetails, JoinType.INNER);
//
//                Join<Product, Category> categoryJoin = root.join(Product_.productDetails.category, JoinType.INNER);
//
//                Join<Product, Image> productsImagesJoin = root.join(Product_.images, JoinType.INNER);
////                Join<Product, Image> productsImagesJoin = (Join<Product, Image>) root.fetch(Product_.images, JoinType.INNER);
//
//
//                List<Predicate> predicates = new ArrayList<>();
//
//
////                productsImagesJoin.on(criteriaBuilder.equal(root.get(Product_.id), 12));
//
//                if (category != null) {
//                    predicates.add(criteriaBuilder.and(categoryJoin.get(Category_.name).in(category)));
//                }
//
//                if (!CollectionUtils.isEmpty(lensColor)) {
//                    predicates.add(criteriaBuilder.and(root.get("lensColor").in(lensColor)));
//                }
//                if (!CollectionUtils.isEmpty(frameColor)) {
//                    predicates.add(criteriaBuilder.and(root.get("frameColor").in(frameColor)));
//                }
//                if (minPrice != null && maxPrice != null) {
//                    predicates.add(criteriaBuilder.between(productDetailsJoin.get(ProductDetails_.price), minPrice, maxPrice));
//                }
//
//                if (sortDirection.equalsIgnoreCase("desc")) {
//                    if (sortBy.equalsIgnoreCase("price")) {
//                        query.orderBy(criteriaBuilder.desc(productDetailsJoin.get(ProductDetails_.price)));
//                    } else if (sortBy.equalsIgnoreCase("productnumber")) {
//                        query.orderBy(criteriaBuilder.desc(root.get("productNumber")));
//                    }
//                } else {
//                    if (sortBy.equalsIgnoreCase("price")) {
//                        query.orderBy(criteriaBuilder.asc(productDetailsJoin.get(ProductDetails_.price)));
//                    } else if (sortBy.equalsIgnoreCase("productnumber")) {
//                        query.orderBy(criteriaBuilder.asc(root.get("productNumber")));
//                    }
//                }
//
////                Predicate predicateImages = productsImagesJoin.get(Image_.id).in(Product_.id);
////                Predicate predicateImages = criteriaBuilder.isEmpty(root.get(Product_.images));
////                Predicate predicateImages = criteriaBuilder.isNotEmpty(root.get(Product_.images));
////                query.where(predicateImages).distinct(true);
////                predicates.add(predicateImages);
////                Predicate predicateImages2 = criteriaBuilder.isEmpty(root.get(Product_.images));
////                query.where(predicateImages2).distinct(true);
////                predicates.add(predicateImages2);
//
////                List<Order> orderByList = new ArrayList<>();
////                orderByList.add(criteriaBuilder.desc(root.get(orderBy)));
////                query.orderBy(orderByList);
//
//                System.out.println("CRITERIA BUILDER!!!");
//                System.out.println(criteriaBuilder);
//                System.out.println(root.get("images").toString());
//
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        };
//    }


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


//    public List<Product> getProductsByCategory(String categoryName) {
//        return productDAO.getAllByCategoryName(categoryName);
//    }

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
