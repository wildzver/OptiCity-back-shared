package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;

@Entity
@Table(name = "lens_colors")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LensColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    //    @Analyzer(impl= KeywordAnalyzer.class)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "customanalyzer"))
    @Field(name = "name_Filter", index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "filterAnalyzer"))
    String name;

    //    @Field(name = "uaName_Filter", index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "filterAnalyzer"))
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "customanalyzer"))
    String uaName;

    String imageName;

}
