package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.util.function.Supplier;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(exclude = {"product"})
@Table(name = "images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    String imageName;

    String imageType;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    boolean isMainImage;

//    @JsonBackReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    Product product;
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

    public Image(String imageName) {
        this.imageName = imageName;
    }
}
