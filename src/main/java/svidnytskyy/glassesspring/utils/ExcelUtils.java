package svidnytskyy.glassesspring.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import svidnytskyy.glassesspring.models.Product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {
    public static ByteArrayInputStream productsToExcel(List<Product> products) throws IOException {
        String[] COLUMNs = {"id", "product_details_id", "category_id", "lens_color_id", "frame_color_id"};
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Products");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // CellStyle for Age
            CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getProductDetails().getId());
                row.createCell(2).setCellValue(product.getCategory().getId());
                row.createCell(3).setCellValue(product.getLensColor().getId());
                row.createCell(4).setCellValue(product.getFrameColor().getId());

//                Cell ageCell = row.createCell(3);
//                ageCell.setCellValue(customer.getAge());
//                ageCell.setCellStyle(ageCellStyle);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public static List parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet("products");
            Iterator rows = sheet.iterator();

            List<Product> products = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                // skip header
                if(rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator cellsInRow = currentRow.iterator();

                Product product = new Product();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = (Cell) cellsInRow.next();

                    if(cellIndex==0) { // id
                        product.setId((long) currentCell.getNumericCellValue());
                    } else if(cellIndex==1) { // product_details_id
                        product.getProductDetails().setId((long) currentCell.getNumericCellValue());
                    } else if(cellIndex==2) { // category_id
                        product.getCategory().setId((long) currentCell.getNumericCellValue());
                    } else if(cellIndex==3) { // lens_color_id
                        product.getLensColor().setId((long) currentCell.getNumericCellValue());
                    } else if(cellIndex==4) { // frame_color_id
                        product.getFrameColor().setId((long) currentCell.getNumericCellValue());
                    }

                    cellIndex++;
                }

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
