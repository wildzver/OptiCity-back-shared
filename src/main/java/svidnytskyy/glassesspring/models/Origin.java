package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.Index;


import javax.persistence.*;

@Entity
@Table(name = "origins")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Origin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "customanalyzer"))
    @Field(name = "name_Filter", index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "filterAnalyzer"))
    String name;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "customanalyzer"))
//    @Field(name = "uaName_Filter", index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "filterAnalyzer"))
    String uaName;

}
