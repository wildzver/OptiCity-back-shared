package svidnytskyy.glassesspring.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.ImageDAO;
import svidnytskyy.glassesspring.exceptions.FileStorageException;
import svidnytskyy.glassesspring.exceptions.MyFileNotFoundException;
import svidnytskyy.glassesspring.models.Image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

@Service
public class ImageService {

    @Autowired
    private ImageDAO imageDAO;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("public/upload-dir");

    public Image storeImage(MultipartFile file, boolean isMainImage) throws IOException {
        String imageName = StringUtils.cleanPath(file.getOriginalFilename());
        String imageType = file.getContentType();
//        byte[] data = file.getBytes();
//        String imageUrl = System.getProperty("user.home") + File.separator +
//              /home/mykola/Documents/окуляри/сайт/glasses-spring/upload-dir
//        String imageUrl = rootLocation.toRealPath().toString() + File.separator + file.getOriginalFilename();


        try {
            if(imageName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + imageName);
            }

            InputStream sourcePath = file.getInputStream();
            Path targetPath = this.rootLocation.resolve(file.getOriginalFilename());
            Files.copy(sourcePath, targetPath);

            Image image = new Image(imageName, imageType, isMainImage);

            return imageDAO.save(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
    }

    public  Image getImageByName(String imageName) {
        return imageDAO.getByImageName(imageName);
    }

    public Image getImageById(Long fileId) {
        return imageDAO.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }

}
