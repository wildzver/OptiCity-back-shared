package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.LensColorDAO;
import svidnytskyy.glassesspring.exceptions.FileStorageException;
import svidnytskyy.glassesspring.models.LensColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class LensColorService {
    @Autowired
    private LensColorDAO lensColorDAO;

    public LensColorService(LensColorDAO lensColorDAO) {
        this.lensColorDAO = lensColorDAO;
    }

    //    private final Path lensColorImagesLocation = Paths.get("public/lens-colors-imgs");
    private final Path lensColorImagesLocation = Paths.get(
            System.getProperty("user.home") + File.separator
//                    + "ubuntu" + File.separator
                    + "OptiCity" + File.separator
                    + "public" + File.separator
                    + "lens-colors-imgs" + File.separator);

    public LensColor save(LensColor lensColor, MultipartFile lensColorImage) {
        if (lensColor != null) {
            saveLensColorImg(lensColor, lensColorImage);
        }

        lensColorDAO.save(lensColor);
        return lensColor;
    }

    public LensColor saveLensColorImg(LensColor lensColor,
                                      MultipartFile lensColorImage) {

        String imageName = StringUtils.cleanPath(lensColorImage.getOriginalFilename());

        try {
            if (imageName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + imageName);
            }

            setLensColorImageName(lensColor, lensColorImage);

            File currentFile = findLensColorImgInStore(lensColor);
            if (currentFile.exists()) {
                currentFile.delete();
            }

            InputStream sourcePath = lensColorImage.getInputStream();
            Path targetPath = this.lensColorImagesLocation.resolve(lensColor.getImageName());
            Files.copy(sourcePath, targetPath);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
        return lensColor;
    }

    public LensColor setLensColorImageName(LensColor lensColor,
                                           MultipartFile lensColorImage) {
        String newImageName = "lc_"
                .concat(lensColor.getName())
                .concat(".")
                .concat(lensColorImage.getContentType().split("/")[1]);
        lensColor.setImageName(newImageName);

        return lensColor;
    }

    public File findLensColorImgInStore(LensColor lensColor) {
        return new File(lensColorImagesLocation + "/" + lensColor.getImageName());
    }

    public List<LensColor> findAll() {
        return lensColorDAO.findAll();
    }

    public LensColor getOne(Long id) {
        return lensColorDAO.getOne(id);
    }

    public LensColor update(Long id,
                            LensColor lensColor,
                            MultipartFile lensColorImage) {
        LensColor updatableLensColor = getOne(id);
        String newName = lensColor.getName();
        String newUaName = lensColor.getUaName();
        String newImage = lensColor.getImageName();

        if (newName != null) {
            if (newImage == null) {
                String newImageName = updatableLensColor.getImageName().replace(updatableLensColor.getName(), newName);
                File currentImgFile = findLensColorImgInStore(updatableLensColor);
                if (currentImgFile.exists()) {
                    currentImgFile.renameTo(new File(lensColorImagesLocation + "/" + newImageName));
                }
                updatableLensColor.setImageName(newImageName);
            }
            updatableLensColor.setName(newName);
        }
        if (newUaName != null) updatableLensColor.setUaName(newUaName);
        if (newImage != null) saveLensColorImg(updatableLensColor, lensColorImage);
        return lensColorDAO.save(updatableLensColor);
    }

    public LensColor delete(Long id) {
        LensColor lensColor = getOne(id);
        if (lensColor != null) {
            lensColorDAO.delete(lensColor);
        }
        return lensColor;
    }
}
