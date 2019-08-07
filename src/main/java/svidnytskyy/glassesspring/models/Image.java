package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(exclude = {"products"})
@Table(name = "images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
    Long id;

    String imageName;

    String imageType;

    boolean isMainImage;
//    @Lob
//    byte[] data;

    public Image(String imageName, String imageType) {
        this.imageName = imageName;
        this.imageType = imageType;
    }

    public Image(String imageName, String imageType, boolean isMainImage) {
        this.imageName = imageName;
        this.imageType = imageType;
        this.isMainImage = isMainImage;
    }
}
