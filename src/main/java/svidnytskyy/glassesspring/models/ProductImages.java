//package svidnytskyy.glassesspring.models;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "product_images")
//@Getter
//@Setter
//@EqualsAndHashCode
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class ProductImages {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    Product product;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "image_id")
//    Image image;
//
//    boolean isMainImageABoolean;
//
//    public ProductImages(Product product, Image image) {
//        this.product = product;
//        this.image = image;
//    }
//}
