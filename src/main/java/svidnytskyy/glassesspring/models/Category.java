package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "categories")
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    //    @Analyzer(definition = "customanalyzer")
//    @Field
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "customanalyzer"))
    String name;

    //    @Analyzer(definition = "customanalyzer")
//    @Field
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "customanalyzer"))
    String uaName;

    String imageName;

//    @ManyToMany(fetch = FetchType.LAZY,
//    cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE
//    })


//    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "category")
//    List<Product> products;


}



