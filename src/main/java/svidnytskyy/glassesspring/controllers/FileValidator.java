package svidnytskyy.glassesspring.controllers;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import svidnytskyy.glassesspring.models.Image;


@Component
public class FileValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile bucket = (MultipartFile) target;

        if (bucket != null && bucket.isEmpty()){
            errors.rejectValue("file", "УРААА!");
        }
    }
}