package svidnytskyy.glassesspring.configs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.io.File;
import java.io.IOException;

//@ComponentScan({})
@Configuration
@EnableWebMvc
//@EnableTransactionManagement
@PropertySource(value = {"classpath:application.properties"})
@PropertySource(value = {"classpath:path.properties"})

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/product-image/**")
                .addResourceLocations("file:/home/mykola/Documents/окуляри/сайт/glasses-spring/public/products-imgs/");
        registry.addResourceHandler("/api/category-image/**")
                .addResourceLocations("file:/home/mykola/Documents/окуляри/сайт/glasses-spring/public/categories-imgs/");
        registry.addResourceHandler("/api/lensColor-image/**")
                .addResourceLocations("file:/home/mykola/Documents/окуляри/сайт/glasses-spring/public/lens-colors-imgs/");
        registry.addResourceHandler("/api/frameColor-image/**")
                .addResourceLocations("file:/home/mykola/Documents/окуляри/сайт/glasses-spring/public/frame-colors-imgs/");
//        String pathToStaticResourceFolder = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "окуляри" + File.separator + "сайт" + File.separator + "glasses-spring" + File.separator + "";

    }

    //    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasename("messages");
//        return messageSource;
//    }
//    @Bean(name = "multipartResolver")
//    public CommonsMultipartResolver getResolver() throws IOException {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        //Set the maximum allowed size (in bytes) for each individual file.
        // resolver.setMaxUploadSize(5242880);//5MB

//        resolver.setMaxUploadSize(524288);//0.5MB
//        resolver.setMaxUploadSizePerFile(102400); //10Kb

        //You may also set other available properties.
//        return resolver;
//    }
//    @Bean(name = "multipartResolver")
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setMaxUploadSizePerFile(102400); //10Kb
//        resolver.setDefaultEncoding("UTF-8");
//        resolver.setResolveLazily(true);
//        return resolver;
//    }

//    @Bean
//    public InternalResourceViewResolver viewResolver(){
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setPrefix("/WEB-INF/views/");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }
}
