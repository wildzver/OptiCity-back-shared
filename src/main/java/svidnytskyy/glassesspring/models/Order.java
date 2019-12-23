package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
@ToString(exclude = {"orderList", "user"})
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    //    @NotNull
    String orderNo;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "order",
            orphanRemoval = true)
    List<OrderItem> orderList = new ArrayList<>();

    //    @JsonBackReference
    @Valid
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH,
                    CascadeType.PERSIST,
                    CascadeType.MERGE})
    User user;

    int quantityTotal;

    int total;
    String comment;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH,
                    CascadeType.PERSIST,
                    CascadeType.MERGE})
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
