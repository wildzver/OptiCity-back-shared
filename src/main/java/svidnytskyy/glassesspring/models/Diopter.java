package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;

@Entity
@Table(name = "diopters")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Diopter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

//    @NumericField(forField = "value")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "customanalyzer"))
    @Field(name = "value_Filter", index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "filterAnalyzer"))
    String value;
}
