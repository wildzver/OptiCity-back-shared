package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import svidnytskyy.glassesspring.repositories.PaddedIntegerBridge;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@EqualsAndHashCode/*(exclude = "products")*/
@ToString(exclude = "products")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    //    @Column(unique = true)
    long modelNumber;

    //    @Pattern(regexp = "^[0-9]+$", message = "pattern")
    @SortableField(forField = "price_Sort")
    @NumericField(forField = "price_Sort")
    @Field(name = "price_Sort", index = Index.YES, analyze = Analyze.NO, store = Store.YES/*, bridge = @FieldBridge(impl = PaddedIntegerBridge.class)*//*, normalizer = @Normalizer(definition = "custom_normalizer")*/)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "filterAnalyzer"))
    int price;

    int lensWidth;

    int lensHeight;

//    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "customanalyzer"))
    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    LensMaterial lensMaterial;

    int totalWidth;
    int bracketLength;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(/*name = "category_id"*/)
    Category category;

    @Field(name = "polarization_Filter", index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "filterAnalyzer"))
    boolean polarization;

//    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "customanalyzer"))
    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    FrameMaterial frameMaterial;

//    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "customanalyzer"))
    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    Origin origin;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    Sex sex;

    @JsonIgnore
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "productDetails",
            orphanRemoval = true)
    List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
        product.setProductDetails(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setProductDetails(null);
        System.out.println("METHOD REMOVE WORKS!");
    }


    public ProductDetails(int price) {
        this.price = price;
    }




    //    @Enumerated(EnumType.STRING)
//    @Transient
//    @ManyToOne(fetch = FetchType.LAZY)
//        @JoinTable(name="products",
//            joinColumns = {@JoinColumn(name = "product_id") },
//            inverseJoinColumns = {@JoinColumn(name = "lensColor_id")})
//    @JoinColumn(name = "lensColor_id")
//            Color lensColor;


    //    @Enumerated(EnumType.STRING)
//    @Transient
//    @ManyToOne(fetch = FetchType.LAZY)
//    Color frameColor;


//    public ProductDetails(int article, int price, int lensWidth, int lensHeight, String lensMaterial, String totalWidth, int bracketLength, String frameMaterial, String countryOfOrigin, Product products) {
//        this.article = article;
//        this.price = price;
//        this.lensWidth = lensWidth;
//        this.lensHeight = lensHeight;
//        this.lensMaterial = lensMaterial;
//        this.totalWidth = totalWidth;
//        this.bracketLength = bracketLength;
//        this.frameMaterial = frameMaterial;
//        this.countryOfOrigin = countryOfOrigin;
//        this.products = Stream.of(products).collect(Collectors.toSet());
//        this.products.forEach(productDetails -> productDetails.setProductDetails(this));
//    }

    //    @Column(name = "lensColor")
//    @Access(AccessType.PROPERTY)
//    protected String getLensColor(){
//        System.out.println("LENSGETTTER!!!!!");
//        return lensColor.description;
//    }
//
//    protected void setLensColor(int id) {
//        System.out.println("LENSSETTTTER!!!");
//        this.lensColor = Colors.getById(id);
//    }
//
//    @Column(name = "frameColor")
//    @Access(AccessType.PROPERTY)
//    protected String getFrameColor() {
//        System.out.println("FRAMEGETTTER!!!!!");
//        return frameColor.description;
//    }
//
//    protected void setFrameColor(int id) {
//        System.out.println("LENSSETTTTER!!!");
//        this.frameColor = Colors.getById(id);
//    }

//    public Product(int article, Category category, int price, int lensWidth, int lensHeight, String lensMaterial, String totalWidth, int bracketLength, String frameMaterial, String countryOfOrigin) {
//        this.article = article;
//        this.category = category;
//        this.price = price;
//        this.lensWidth = lensWidth;
//        this.lensHeight = lensHeight;
//        this.lensMaterial = lensMaterial;
//        this.totalWidth = totalWidth;
//        this.bracketLength = bracketLength;
//        this.frameMaterial = frameMaterial;
//        this.countryOfOrigin = countryOfOrigin;
//    }

//    @Override
//    public String toString() {
//        return "Product{" +
//                "id=" + id +
//                ", article=" + article +
//                ", category='" + category + '\'' +
//                ", price=" + price +
//                ", lensColor=" + lensColor +
//                ", lensWidth=" + lensWidth +
//                ", lensHeight=" + lensHeight +
//                ", lensMaterial='" + lensMaterial + '\'' +
//                ", totalWidth='" + totalWidth + '\'' +
//                ", bracketLength=" + bracketLength +
//                ", frameColor=" + frameColor +
//                ", frameMaterial='" + frameMaterial + '\'' +
//                ", countryOfOrigin='" + countryOfOrigin + '\'' +
//                ", creationTime=" + creationTime +
//                '}';
//    }
}
