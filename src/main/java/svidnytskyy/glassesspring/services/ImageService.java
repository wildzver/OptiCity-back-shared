package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.ImageDAO;
import svidnytskyy.glassesspring.exceptions.FileStorageException;
import svidnytskyy.glassesspring.models.Image;
import svidnytskyy.glassesspring.models.Product;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ImageService {

    @Autowired
    private ImageDAO imageDAO;

//    private final Path productsImagesLocation = Paths.get("public/products-imgs");
    private final Path productsImagesLocation = Paths.get(System.getProperty("user.home") + File.separator
        + "ubuntu" + File.separator
        + "OptiCity" + File.separator
        + "public" + File.separator
        + "products-imgs" + File.separator);

    public Image storeProductsImages(Product product,
                                     MultipartFile file,
                                     boolean isMainImage) {
        String imageName = StringUtils.cleanPath(file.getOriginalFilename());
        String imageType = file.getContentType();
        try {

            AtomicLong freeId = new AtomicLong(0);

            StringBuilder newImageName = new StringBuilder();

            do {
                newImageName.setLength(0);
                newImageName
                        .append(product.getProductDetails().getModelNumber())
                        .append("_")
                        .append(product.getLensColor().getId())
                        .append("_")
                        .append(product.getFrameColor().getId())
                        .append(isMainImage == true ? "_main" : "")
                        .append(isMainImage == true ? "" : "_" + freeId.getAndIncrement())
                        .append(".")
                        .append(file.getContentType().split("/")[1]);

                if (isMainImage == true && findProductImageInStore(newImageName.toString()).exists()) {
                    findProductImageInStore(newImageName.toString()).delete();
                }
            } while (findProductImageInStore(newImageName.toString()).exists());

            InputStream sourcePath = file.getInputStream();
            Path targetPath = this.productsImagesLocation.resolve(newImageName.toString());
            Files.copy(sourcePath, targetPath);

            Image image = new Image(newImageName.toString(), imageType, isMainImage);

            return imageDAO.saveAndFlush(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
    }

    public void deleteProductImage(long id) {
        File currentFile = findProductImageInStore(imageDAO.getOne(id).getImageName());
        if (currentFile.exists()) {
            currentFile.delete();
        }

        imageDAO.deleteById(id);
    }

    public File findProductImageInStore(String imageName) {
        return new File(productsImagesLocation + "/" + imageName);
    }
}
