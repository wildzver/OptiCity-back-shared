package svidnytskyy.glassesspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import svidnytskyy.glassesspring.models.Order;

public interface OrderDAO extends JpaRepository<Order, Long> {
}
