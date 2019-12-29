package svidnytskyy.glassesspring.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

@Configuration
@EnableWebMvc
//@PropertySource(value = {"classpath:application.properties"})
//@PropertySource(value = {"classpath:path.properties"})

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        URI pathToPublicResourceFolder = Paths.get(
                System.getProperty("user.home")
                + File.separator + "Documents"
                + File.separator + "окуляри"
                + File.separator + "сайт"
                + File.separator + "glasses-spring"
                + File.separator + "public"
                + File.separator)
                .toUri();

//        registry.addResourceHandler("/*.html", "/*.css", "/*.js", "/*.svg", "/assets/images/**")
        registry.addResourceHandler("/**")
                .addResourceLocations("file:/home/mykola/Documents/окуляри/сайт/glasses-spring/src/main/resources/static/");
        registry.addResourceHandler("/api/product-image/**")
                .addResourceLocations(pathToPublicResourceFolder + "products-imgs" + File.separator);
//                .addResourceLocations("file:/home/mykola/Documents/окуляри/сайт/glasses-spring/public/products-imgs/");
        registry.addResourceHandler("/api/category-image/**")
                .addResourceLocations(pathToPublicResourceFolder + "categories-imgs" + File.separator);
        registry.addResourceHandler("/api/lensColor-image/**")
                .addResourceLocations(pathToPublicResourceFolder + "lens-colors-imgs" + File.separator);
        registry.addResourceHandler("/api/frameColor-image/**")
                .addResourceLocations(pathToPublicResourceFolder + "frame-colors-imgs" + File.separator);
    }
}
