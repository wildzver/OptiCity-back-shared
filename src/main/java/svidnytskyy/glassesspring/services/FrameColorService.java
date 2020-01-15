package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.FrameColorDAO;
import svidnytskyy.glassesspring.exceptions.FileStorageException;
import svidnytskyy.glassesspring.models.FrameColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FrameColorService {
    @Autowired
    private FrameColorDAO frameColorDAO;

    public FrameColorService(FrameColorDAO frameColorDAO) {
        this.frameColorDAO = frameColorDAO;
    }

    //    private final Path frameColorImagesLocation = Paths.get("public/frame-colors-imgs");
    private final Path frameColorImagesLocation = Paths.get(
            System.getProperty("user.home") + File.separator
                    + "ubuntu" + File.separator
                    + "OptiCity" + File.separator
                    + "public" + File.separator
                    + "frame-colors-imgs" + File.separator);

    public FrameColor save(FrameColor frameColor,
                           MultipartFile frameColorImage) {
        if (frameColor != null) {
            saveFrameColorImg(frameColor, frameColorImage);
        }
        frameColorDAO.save(frameColor);
        return frameColor;
    }

    public FrameColor saveFrameColorImg(FrameColor frameColor,
                                        MultipartFile frameColorImage) {

        String imageName = StringUtils.cleanPath(frameColorImage.getOriginalFilename());

        try {
            if (imageName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + imageName);
            }

            setFrameColorImageName(frameColor, frameColorImage); // return category with new imageName

            File currentFile = findFrameColorImgInStore(frameColor);
            if (currentFile.exists()) {
                currentFile.delete();
            }

            InputStream sourcePath = frameColorImage.getInputStream();
            Path targetPath = this.frameColorImagesLocation.resolve(frameColor.getImageName());
            Files.copy(sourcePath, targetPath);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
        return frameColor;
    }

    public FrameColor setFrameColorImageName(FrameColor frameColor,
                                             MultipartFile frameColorImage) {
        String newImageName = "fc_"
                .concat(frameColor.getName())
                .concat(".")
                .concat(frameColorImage.getContentType().split("/")[1]);
        frameColor.setImageName(newImageName);

        return frameColor;
    }

    public File findFrameColorImgInStore(FrameColor frameColor) {
        return new File(frameColorImagesLocation + "/" + frameColor.getImageName());
    }

    public List<FrameColor> findAll() {
        return frameColorDAO.findAll();
    }

    public FrameColor getOne(Long id) {
        return frameColorDAO.getOne(id);
    }

    public FrameColor update(Long id,
                             FrameColor frameColor,
                             MultipartFile frameColorImage) {
        FrameColor updatableFrameColor = getOne(id);
        String newName = frameColor.getName();
        String newUaName = frameColor.getUaName();
        String newImage = frameColor.getImageName();

        if (newName != null) {
            if (newImage == null) {
                String newImageName = updatableFrameColor.getImageName().replace(updatableFrameColor.getName(), newName);
                File currentImgFile = findFrameColorImgInStore(updatableFrameColor);
                if (currentImgFile.exists()) {
                    currentImgFile.renameTo(new File(frameColorImagesLocation + "/" + newImageName));
                }
                updatableFrameColor.setImageName(newImageName);
            }
            updatableFrameColor.setName(newName);
        }
        if (newUaName != null) updatableFrameColor.setUaName(newUaName);
        if (newImage != null) saveFrameColorImg(updatableFrameColor, frameColorImage);
        return frameColorDAO.save(updatableFrameColor);
    }

    public FrameColor delete(Long id) {
        FrameColor frameColor = getOne(id);
        if (frameColor != null) {
            frameColorDAO.delete(frameColor);
        }
        return frameColor;
    }
}
