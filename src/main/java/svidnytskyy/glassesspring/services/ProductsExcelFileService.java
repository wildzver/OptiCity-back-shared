package svidnytskyy.glassesspring.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.*;
import svidnytskyy.glassesspring.models.*;
import svidnytskyy.glassesspring.utils.ExcelUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsExcelFileService {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    LensColorDAO lensColorDAO;

    @Autowired
    FrameColorDAO frameColorDAO;

    @Autowired
    LensMaterialDAO lensMaterialDAO;

    @Autowired
    FrameMaterialDAO frameMaterialDAO;

    @Autowired
    OriginDAO originDAO;

    @Autowired
    SexDAO sexDAO;

    @Autowired
    DiopterDAO diopterDAO;

    public void store(MultipartFile file) {
        try {
            System.out.println(file.getOriginalFilename() + "STORED!!!");
            List<Product> products = parseExcelFile(file.getInputStream());
            System.out.println("ALL MY UPLOADED PRODUCTS" + products.toString());

            productService.saveProductsWithoutImages(products);
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }

    // Load Data to Excel File
    public ByteArrayInputStream loadFile() {
        List<Product> products = (List<Product>) productDAO.findAll();

        try {
            ByteArrayInputStream in = ExcelUtils.productsToExcel(products);
            return in;
        } catch (IOException e) {
        }

        return null;
    }

    public List<Product> parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet("products");
            Iterator rows = sheet.iterator();

            List<Product> products = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                // skip header
                if (rowNumber == 0 || rowNumber == 1) {
                    rowNumber++;
                    continue;
                }

                Iterator cellsInRow = currentRow.iterator();

                Product product = new Product();
                ProductDetails productDetails = new ProductDetails();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = (Cell) cellsInRow.next();

//                    if (cellIndex == 0) { // id
//                        product.setId((long) currentCell.getNumericCellValue());
//                    } else
                    if (cellIndex == 0) { // product_details.model_number
                        productDetails.setModelNumber((int) currentCell.getNumericCellValue());
                    } else if (cellIndex == 1) { // product_details.price
                        productDetails.setPrice((int) currentCell.getNumericCellValue());
                        System.out.println("PRODUCT PRODUCTDETAILS!!!" + (long) currentCell.getNumericCellValue());
                    } else if (cellIndex == 2) { // category_id
                        System.out.println("asd" + (long) currentCell.getNumericCellValue());
                        Optional currentCategory = categoryDAO.findById((long) currentCell.getNumericCellValue());
                        try {
                            System.out.println("CURRENT CATEGORY!" + currentCategory.get());
                            productDetails.setCategory((Category) currentCategory.get());
                        } catch (NoSuchElementException e) {
                            continue;
                        }
                        System.out.println("PRODUCT CATEGORY!!!" + productDetails.getCategory());
                    } else if (cellIndex == 3) { // product_details.lens_width
                        productDetails.setLensWidth((int) currentCell.getNumericCellValue());
                        System.out.println("PRODUCT PRODUCTDETAILS!!!" + (long) currentCell.getNumericCellValue());
                    } else if (cellIndex == 4) { // product_details.lens_height
                        productDetails.setLensHeight((int) currentCell.getNumericCellValue());
                        System.out.println("PRODUCT PRODUCTDETAILS!!!" + (long) currentCell.getNumericCellValue());
                    } else if (cellIndex == 5) { // product_details.lens_material
                        Optional currentLensMaterial = lensMaterialDAO.findById((long) currentCell.getNumericCellValue());
                        try {
                            productDetails.setLensMaterial((LensMaterial) currentLensMaterial.get());
                            System.out.println("PRODUCT PRODUCTDETAILS!!!" + currentLensMaterial.get());

                        } catch (NoSuchElementException e) {
                            continue;
                        }
                    } else if (cellIndex == 6) { // product_details.total_width
                        productDetails.setTotalWidth((int) currentCell.getNumericCellValue());
                        System.out.println("PRODUCT PRODUCTDETAILS!!!" + (long) currentCell.getNumericCellValue());
                    } else if (cellIndex == 7) { // product_details.bracket_length
                        productDetails.setBracketLength((int) currentCell.getNumericCellValue());
                        System.out.println("PRODUCT PRODUCTDETAILS!!!" + (long) currentCell.getNumericCellValue());
                    } else if (cellIndex == 8) { // product_details.frame_material
                        Optional currentFrameMaterial = frameMaterialDAO.findById((long) currentCell.getNumericCellValue());
                        try {
                            productDetails.setFrameMaterial((FrameMaterial) currentFrameMaterial.get());
                            System.out.println("PRODUCT PRODUCTDETAILS!!!" + (long) currentCell.getNumericCellValue());
                        } catch (NoSuchElementException e) {
                            continue;
                        }
                    } else if (cellIndex == 9) { // product_details.polarization
                        productDetails.setPolarization(currentCell.getBooleanCellValue());
                        System.out.println("PRODUCT PRODUCTDETAILS!!!" + currentCell.getBooleanCellValue());
                    } else if (cellIndex == 10) { // product_details.origin
                        Optional currentOrigin = originDAO.findById((long) currentCell.getNumericCellValue());
                        try {
                            productDetails.setOrigin((Origin) currentOrigin.get());
                            System.out.println("PRODUCT PRODUCTDETAILS!!!" + (long) currentCell.getNumericCellValue());
                        } catch (NoSuchElementException e) {
                            continue;
                        }
                    } else if (cellIndex == 11) { // product_details.sex
                        Optional currentSex = sexDAO.findById((long) currentCell.getNumericCellValue());
                        try {
                            productDetails.setSex((Sex) currentSex.get());
                            System.out.println("PRODUCT PRODUCTDETAILS!!!" + (long) currentCell.getNumericCellValue());
                        } catch (NoSuchElementException e) {
                            continue;
                        }
                        product.setProductDetails(productDetails);
                        System.out.println("PRODUCT PRODUCTDETAILS FULL!!!" + productDetails);
                    } else if (cellIndex == 12) { // lens_color_id
                        Optional currentLensColor = lensColorDAO.findById((long) currentCell.getNumericCellValue());
                        try {
                            product.setLensColor((LensColor) currentLensColor.get());
                        } catch (NoSuchElementException e) {
                            continue;
                        }
                    } else if (cellIndex == 13) { // frame_color_id
                        Optional currentFrameColor = frameColorDAO.findById((long) currentCell.getNumericCellValue());
                        try {
                            product.setFrameColor((FrameColor) currentFrameColor.get());
                        } catch (NoSuchElementException e) {
                            continue;
                        }
                    } else if (cellIndex == 14) { // diopter_id
                        String diopter = new DataFormatter().formatCellValue(currentCell).trim();
                        System.out.println("MY DIOPTERS EXCEL INPUT0" + diopter);
                        List<Diopter> diopters = new ArrayList<>();
                        if (!diopter.toLowerCase().contains("null")) {
                            if (diopter.contains(",")) {
                                String[] dioptersInput = (diopter.split(","));
                                System.out.println("MY DIOPTERS EXCEL INPUT" + dioptersInput);
                                Arrays.stream(dioptersInput).peek(diopterId -> {
                                    Diopter currentDiopter = diopterDAO.getOne(Long.parseLong(diopterId));
                                    if (currentDiopter != null) diopters.add(currentDiopter);
                                }).collect(Collectors.toList());
                            } else if (diopter.matches("[0-9]+")) {
                                Optional currentDiopter = diopterDAO.findById(Long.parseLong(diopter));
                                try {
                                    System.out.println("MY CURRENT DIOPTER<>" + currentDiopter.get());

                                    diopters.add((Diopter) currentDiopter.get());
                                } catch (NoSuchElementException e) {
                                    System.out.println("NoSuchElementException WORKS!");
                                    continue;
                                }
                            }
                            System.out.println("MY DIOPTERS EXCEL" + diopters);
                            product.setDiopters(diopters);
                        }
                    }

                    cellIndex++;

                }

                System.out.println("PRODUCT!" + product);

                products.add(product);
            }

            // Close WorkBook
            workbook.close();

            return products;
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }
}
