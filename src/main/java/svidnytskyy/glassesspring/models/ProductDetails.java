package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "products")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ProductDetails extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    long modelNumber;

    @SortableField(forField = "price_Sort")
    @NumericField(forField = "price_Sort")
    @Field(name = "price_Sort", index = Index.YES, analyze = Analyze.NO, store = Store.YES/*, bridge = @FieldBridge(impl = PaddedIntegerBridge.class)*//*, normalizer = @Normalizer(definition = "custom_normalizer")*/)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "filterAnalyzer"))
    int price;

    int lensWidth;

    int lensHeight;

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

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    FrameMaterial frameMaterial;

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
}
