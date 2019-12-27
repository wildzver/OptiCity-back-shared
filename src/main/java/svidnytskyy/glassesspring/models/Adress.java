package svidnytskyy.glassesspring.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString()
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Adress extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String settlement;

    String deliveryMethod = DeliveryMethod.NOVA_POSHTA.name();

    String branch;

    public Adress(String settlement, String branch) {
        this.settlement = settlement;
        this.branch = branch;
    }
}
