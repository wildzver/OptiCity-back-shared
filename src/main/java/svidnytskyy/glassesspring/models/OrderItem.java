package svidnytskyy.glassesspring.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Product product;

    int quantity;

    int subTotal;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Order order;

//    public OrderItem(int quantity, int subTotal) {
//        this.quantity = quantity;
//        this.subTotal = subTotal;
//    }

//    public OrderItem(Product product, int quantity, int subTotal) {
//        this.product = product;
//        this.quantity = quantity;
//        this.subTotal = subTotal;
//    }
}

