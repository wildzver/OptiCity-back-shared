package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;
import svidnytskyy.glassesspring.repositories.ProductNumberBridge;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Indexed
@AnalyzerDefs({
        @AnalyzerDef(name = "customanalyzer",
                tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = EdgeNGramFilterFactory.class,
                                params = {
                                        @Parameter(name = "minGramSize", value = "1"),
                                        @Parameter(name = "maxGramSize", value = "3")
                                }),
                }),
        @AnalyzerDef(name = "filterAnalyzer",
                tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                }),
})

@NormalizerDef(name = "custom_normalizer",
        filters = {
                @TokenFilterDef(factory = StandardFilterFactory.class),
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class), // Replace accented characeters by their simpler counterpart (è => e, etc.)
                @TokenFilterDef(factory = LowerCaseFilterFactory.class), // Lowercase all characters
        })

@Table(name = "products")
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = {"images", "productDetails"})
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false, updatable = false)
    private String uuid;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(/*name = "product_details_id"*/)
    ProductDetails productDetails;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(/*name = "lens_color_id"*/)
    LensColor lensColor;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(/*name = "frame_color_id"*/)
    FrameColor frameColor;

    @IndexedEmbedded
    @ManyToMany(fetch = FetchType.LAZY)
    List<Diopter> diopters = new ArrayList<>();

    @IndexedEmbedded
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "product_id")
    List<Image> images = new ArrayList<>();

    @SortableField(forField = "productNumber_Sort")
    @Field(name = "productNumber_Sort", index = Index.YES, analyze = Analyze.NO, store = Store.YES, normalizer = @Normalizer(definition = "custom_normalizer"), bridge = @FieldBridge(impl = ProductNumberBridge.class))
    @Field(name = "productNumber", index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "customanalyzer"))
    String productNumber;

    public Product(List<Image> images) {
        this.images = images;
    }

    public Product(String productNumber) {
        this.productNumber = productNumber;
    }

    public Product(ProductDetails productDetails,
                   LensColor lensColor,
                   FrameColor frameColor,
                   String productNumber) {
        this.productDetails = productDetails;
        this.lensColor = lensColor;
        this.frameColor = frameColor;
        this.productNumber = productNumber;
    }

    public Product(ProductDetails productDetails,
                   List<Image> images,
                   String productNumber) {
        this.productDetails = productDetails;
        this.images = images;
        this.productNumber = productNumber;
    }

    public Product(ProductDetails productDetails,
                   LensColor lensColor,
                   FrameColor frameColor,
                   List<Diopter> diopters,
                   String productNumber) {
        this.productDetails = productDetails;
        this.lensColor = lensColor;
        this.frameColor = frameColor;
        this.diopters = diopters;
        this.productNumber = productNumber;
    }
}
