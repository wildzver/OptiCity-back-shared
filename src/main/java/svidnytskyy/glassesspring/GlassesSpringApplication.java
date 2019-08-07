package svidnytskyy.glassesspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import svidnytskyy.glassesspring.services.ImageService;
import svidnytskyy.glassesspring.services.StorageService;

import javax.annotation.Resource;

@SpringBootApplication
@EnableJpaAuditing
public class GlassesSpringApplication implements CommandLineRunner {
	@Resource
	ImageService imageService;

	public static void main(String[] args) {
		SpringApplication.run(GlassesSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		imageService.deleteAll();
//		imageService.init();
	}
}
