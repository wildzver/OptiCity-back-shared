package svidnytskyy.glassesspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class GlassesSpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GlassesSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
