package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.Adress;

import java.util.List;

public interface AdressDAO extends JpaRepository<Adress, Long> {
    List<Adress> getAdressesBySettlementAndDeliveryMethodAndBranch(String settlement, String deliveryMethod, String branch);
}
