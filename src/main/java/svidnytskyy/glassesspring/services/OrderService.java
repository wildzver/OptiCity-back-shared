package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.AdressDAO;
import svidnytskyy.glassesspring.dao.OrderDAO;
import svidnytskyy.glassesspring.dao.ProductDAO;
import svidnytskyy.glassesspring.dao.UserDAO;
import svidnytskyy.glassesspring.models.Adress;
import svidnytskyy.glassesspring.models.DeliveryMethod;
import svidnytskyy.glassesspring.models.Order;
import svidnytskyy.glassesspring.models.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    public OrderService() {
    }

    @Autowired
    private OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Autowired
    private ProductDAO productDAO;

    public OrderService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Autowired
    private AdressDAO adressDAO;

    public OrderService(AdressDAO adressDAO) {
        this.adressDAO = adressDAO;
    }


    @Autowired
    private UserDAO userDAO;

    @Autowired
    EmailsService emailsService;

    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    public Order save(Order order) throws ParseException, IOException, MessagingException {
        if (order != null) {
            AtomicInteger quantityTotal = new AtomicInteger();
            AtomicInteger total = new AtomicInteger();
            order.getOrderList().forEach(orderItem -> {
                orderItem.setOrder(order);
                orderItem.setProduct(productDAO.getOneByUuid(orderItem.getProduct().getUuid()));
                orderItem.setSubTotal(orderItem.getQuantity() * orderItem.getProduct().getProductDetails().getPrice());
                quantityTotal.addAndGet(orderItem.getQuantity());
                total.addAndGet(orderItem.getQuantity() * orderItem.getProduct().getProductDetails().getPrice());
            });
            order.setQuantityTotal(quantityTotal.intValue());
            order.setTotal(total.intValue());
            if (order.getAdress().getSettlement().isEmpty() || order.getAdress().getBranch().isEmpty()) {
                order.getAdress().setDeliveryMethod(DeliveryMethod.FROM_STORE.name());
            } else order.getAdress().setDeliveryMethod(DeliveryMethod.NOVA_POSHTA.name());

            if (order.getUser().getEmail().equalsIgnoreCase("tarassvidnytskyy@gmail.com")) {
                order.setUser(userDAO.getOneByEmail("tarassvidnytskyy@gmail.com"));
            }

            List<User> currentUsers = userDAO.getUsersByFirstNameAndLastNameAndEmailAndPhone(
                    order.getUser().getFirstName(),
                    order.getUser().getLastName(),
                    order.getUser().getEmail(),
                    order.getUser().getPhone());

            if (!currentUsers.isEmpty()) order.setUser(currentUsers.get(0));
            List<Adress> currentAdresses = adressDAO.getAdressesBySettlementAndDeliveryMethodAndBranch(order.getAdress().getSettlement(), order.getAdress().getDeliveryMethod(), order.getAdress().getBranch());
            if (!currentAdresses.isEmpty()) order.setAdress(currentAdresses.get(0));
            Order orderFlush = orderDAO.saveAndFlush(order);
            order.setOrderNo(String.valueOf(orderFlush.getId()));
            orderDAO.save(order);
        }
        emailsService.sendEmail(order);
        return order;
    }
}
