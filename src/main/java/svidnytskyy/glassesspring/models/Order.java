package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orderList")
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String orderNo;

    //    @JsonIgnore
//    @JsonIgnoreProperties("order")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
    List<OrderItem> orderList = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    User user;

    int quantityTotal;

    int total;
    String comment;

    //    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Adress adress;

    public Order(String orderNo, List<OrderItem> orderList) {
        this.orderNo = orderNo;
        this.orderList = orderList;
    }

    public Order(String orderNo, List<OrderItem> orderList, Adress adress) {
        this.orderNo = orderNo;
        this.orderList = orderList;
        this.adress = adress;
    }

    public Order(String orderNo, List<OrderItem> orderList, int quantityTotal, int total, Adress adress) {
        this.orderNo = orderNo;
        this.orderList = orderList;
        this.quantityTotal = quantityTotal;
        this.total = total;
        this.adress = adress;
    }

}
