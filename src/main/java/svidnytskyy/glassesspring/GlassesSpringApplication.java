package svidnytskyy.glassesspring;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class GlassesSpringApplication implements CommandLineRunner {

	public static void main(String[] args) throws IOException {

//		Firestore db = FirestoreClient.getFirestore()

		SpringApplication.run(GlassesSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
