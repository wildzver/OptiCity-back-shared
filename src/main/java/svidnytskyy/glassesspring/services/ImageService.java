package svidnytskyy.glassesspring.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import svidnytskyy.glassesspring.controllers.FileValidator;
import svidnytskyy.glassesspring.dao.ImageDAO;
import svidnytskyy.glassesspring.exceptions.FileStorageException;
import svidnytskyy.glassesspring.exceptions.MyFileNotFoundException;
import svidnytskyy.glassesspring.models.Image;
import svidnytskyy.glassesspring.models.LensColor;
import svidnytskyy.glassesspring.models.Product;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
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

//    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private final Path productsImagesLocation = Paths.get("public/products-imgs");

    public Image storeProductsImages(Product product,
                                     MultipartFile file,
                                     boolean isMainImage) {
        String imageName = StringUtils.cleanPath(file.getOriginalFilename());
        String imageType = file.getContentType();
        try {

            AtomicLong freeId = new AtomicLong(0);

            StringBuilder newImageName = new StringBuilder();

            do {
//                if (isMainImage && )
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

            System.out.println("MY NEW IMAGE NAME" + newImageName);
//        byte[] data = file.getBytes();
//        String imageUrl = System.getProperty("user.home") + File.separator +
//              /home/mykola/Documllents/окуляри/сайт/glasses-spring/upload-dir
//        String imageUrl = productsImagesLocation.toRealPath().toString() + File.separator + file.getOriginalFilename();

//            if (imageName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + imageName);
//            }
            InputStream sourcePath = file.getInputStream();
            Path targetPath = this.productsImagesLocation.resolve(newImageName.toString());
            Files.copy(sourcePath, targetPath);

            Image image = new Image(newImageName.toString(), imageType, isMainImage);

            return imageDAO.saveAndFlush(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
    }

    public Image getImageByName(String imageName) {
        return imageDAO.getByImageName(imageName);
    }

    public Image getImageById(Long fileId) {
        return imageDAO.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(productsImagesLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(productsImagesLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
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
