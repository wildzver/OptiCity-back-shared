package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = {"images", "orderItem"})
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_details_id")
    ProductDetails productDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lens_color_id")
    LensColor lensColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frame_color_id")
    FrameColor frameColor;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
//    @JoinColumn(name = "image_id")
    List<Image> images = new ArrayList<>();

    @Column(unique = true)
    String productNumber;

    public Product(String productNumber) {
        this.productNumber = productNumber;
    }
    //    @JsonIgnore
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
//    List<OrderItem> orderItem = new ArrayList<>();
//    public Product(ProductDetails productDetails,
//                   Category category,
//                   LensColor lensColor,
//                   FrameColor frameColor,
//                   String productNumber) {
//        this.productDetails = productDetails;
//        this.category = category;
//        this.lensColor = lensColor;
//        this.frameColor = frameColor;
//        this.productNumber = productNumber;
//    }


}
