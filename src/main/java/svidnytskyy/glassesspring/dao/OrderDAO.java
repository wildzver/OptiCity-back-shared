package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import svidnytskyy.glassesspring.models.Order;

import java.util.Date;

public interface OrderDAO extends JpaRepository<Order, Long> {
//    @Query("select o from Order o where o.createdAt = :createdAt")
    Order getByUserPhoneAndCreatedAt(String phone, Date createdAt);
    Order getByUserPhone(String phone/*, @Param("createdAt") Date createdAt*/);

}
