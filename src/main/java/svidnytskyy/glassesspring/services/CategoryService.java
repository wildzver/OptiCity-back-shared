package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.dao.CategoryDAO;
import svidnytskyy.glassesspring.exceptions.FileStorageException;
import svidnytskyy.glassesspring.models.Category;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

//    private final Path categoriesImagesLocation = Paths.get("public/categories-imgs");
    private final Path categoriesImagesLocation = Paths.get(
        System.getProperty("user.home") + File.separator
//                + "ubuntu" + File.separator
                + "OptiCity" + File.separator
                + "public" + File.separator
                + "categories-imgs" + File.separator);

    public Category save(Category category,
                         MultipartFile categoryImage) {
        if (category != null) {
            saveCategoryImg(category, categoryImage);
        }
        categoryDAO.save(category);
        return category;
    }

    public Category saveCategoryImg(Category category,
                                    MultipartFile categoryImage) {

        String imageName = StringUtils.cleanPath(categoryImage.getOriginalFilename());

        try {
            if (imageName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + imageName);
            }

            setCategoryImageName(category, categoryImage); // returns category with new imageName

            File currentFile = findCategoryImgInStore(category);
            if (currentFile.exists()) {
                currentFile.delete();
            }

            InputStream sourcePath = categoryImage.getInputStream();
            Path targetPath = this.categoriesImagesLocation.resolve(category.getImageName());
            Files.copy(sourcePath, targetPath);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
        return category;
    }

    public Category setCategoryImageName(Category category,
                                         MultipartFile categoryImage) {
        String newImageName = category.getName()
                .concat("_home-catalog.")
                .concat(categoryImage.getContentType().split("/")[1]);
        category.setImageName(newImageName);

        return category;
    }

    public File findCategoryImgInStore(Category category) {
        return new File(categoriesImagesLocation + "/" + category.getImageName());
    }

    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    public Category getOne(long id) {
        return categoryDAO.getOne(id);
    }

    public Category getCategoryByName(String categoryName) {
        Optional<Category> optionalCategory = Optional.ofNullable(categoryDAO.findFirstByName(categoryName)); // returns java8 optional
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        } else {
            System.out.println("MODEL NUMBER DID NOT FOUND");
        }
        return null;
    }

    public Category update(long id,
                           Category category,
                           MultipartFile categoryImage) throws IOException {
        Category updatableCategory = getOne(id);
        String newName = category.getName();
        String newUaName = category.getUaName();
        String newImage = category.getImageName();

        if (newName != null) {
            if (newImage == null) {
                String newImageName = updatableCategory.getImageName().replace(updatableCategory.getName(), newName);
                File currentImgFile = findCategoryImgInStore(updatableCategory);
                if(currentImgFile.exists()) {
                    currentImgFile.renameTo(new File(categoriesImagesLocation + "/" + newImageName));
                }
                updatableCategory.setImageName(newImageName);
            }
            updatableCategory.setName(newName);
        }
        if (newUaName != null) updatableCategory.setUaName(newUaName);
        if (newImage != null) saveCategoryImg(updatableCategory, categoryImage);
        return categoryDAO.save(updatableCategory);
    }

    public Category delete(long id) {
        Category category = getOne(id);
        if (category != null) {
            categoryDAO.delete(category);
        }
        return category;
    }
}
