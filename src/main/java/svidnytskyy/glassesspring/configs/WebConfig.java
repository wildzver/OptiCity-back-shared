package svidnytskyy.glassesspring.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
@EnableWebMvc

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String pathToPublicResourceFolder = "file://"
                + System.getProperty("user.home") + File.separator
                + "OptiCity" + File.separator
                + "public" + File.separator;

        String pathToStaticResourceFolder = "file://"
                + System.getProperty("user.home") + File.separator
                + "OptiCity" + File.separator
                + "static" + File.separator;

        System.out.println("My URI PUBLIC" + pathToPublicResourceFolder);
        System.out.println("My URI STATIC" + pathToStaticResourceFolder);

        registry.addResourceHandler("/api/product-image/**")
                .addResourceLocations(pathToPublicResourceFolder + File.separator + "products-imgs" + File.separator);

        registry.addResourceHandler("/api/category-image/**")
                .addResourceLocations(pathToPublicResourceFolder + File.separator + "categories-imgs" + File.separator);

        registry.addResourceHandler("/api/lensColor-image/**")
                .addResourceLocations(pathToPublicResourceFolder + File.separator + "lens-colors-imgs" + File.separator);

        registry.addResourceHandler("/api/frameColor-image/**")
                .addResourceLocations(pathToPublicResourceFolder + File.separator + "frame-colors-imgs" + File.separator);
    }
}
