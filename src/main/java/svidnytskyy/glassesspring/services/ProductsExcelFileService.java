package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.ProductDAO;
import svidnytskyy.glassesspring.models.Product;
import svidnytskyy.glassesspring.utils.ExcelUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ProductsExcelFileService {

    @Autowired
    ProductDAO productDAO;

    public void store(MultipartFile file){
        try {
            List<Product> products = ExcelUtils.parseExcelFile(file.getInputStream());
            // Save Customers to DataBase
            productDAO.saveAll(products);
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
        } catch (IOException e) {}

        return null;
    }
}
