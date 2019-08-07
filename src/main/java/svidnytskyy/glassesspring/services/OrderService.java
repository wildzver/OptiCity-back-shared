package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svidnytskyy.glassesspring.dao.OrderDAO;
import svidnytskyy.glassesspring.dao.ProductDAO;
import svidnytskyy.glassesspring.models.DeliveryMethod;
import svidnytskyy.glassesspring.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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

    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    public Order save(Order order) throws ParseException {
        if (order != null) {
            AtomicInteger quantityTotal = new AtomicInteger();
            AtomicInteger total = new AtomicInteger();
            order.getOrderList().forEach(orderItem -> {
                orderItem.setOrder(order);
                orderItem.setProduct(productDAO.getOneByProductNumber(orderItem.getProduct().getProductNumber()));
                orderItem.setSubTotal(orderItem.getQuantity() * orderItem.getProduct().getProductDetails().getPrice());
                quantityTotal.addAndGet(orderItem.getQuantity());
                total.addAndGet(orderItem.getQuantity() * orderItem.getProduct().getProductDetails().getPrice());
            });
            order.setQuantityTotal(quantityTotal.intValue());
            order.setTotal(total.intValue());
            if (order.getAdress().getSettlement().isEmpty() || order.getAdress().getBranch().isEmpty()) {
                order.getAdress().setDeliveryMethod(DeliveryMethod.FROM_STORE.name());
            } else order.getAdress().setDeliveryMethod(DeliveryMethod.NOVA_POSHTA.name());
            orderDAO.save(order);

            Order currentOrder = getByUserPhoneAndCreatedAt(order.getUser().getPhone(),
                    (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreatedAt()))));
            Date currentOrderDate = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(order.getCreatedAt()));

            System.out.println("CURRENT ORDER!" + currentOrder);
            System.out.println("CURRENT ORDER!" + order.getUser().getPhone());
            System.out.println("CURRENT ORDER!" + order.getCreatedAt());
            System.err.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(shiftTimeZone(order.getCreatedAt(), TimeZone.getTimeZone("EST"), TimeZone.getTimeZone("UTC"))));
            System.err.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreatedAt()));

            order.setOrderNo("asd");


            orderDAO.save(order);

        }
        return order;
    }


    public Order getByUserPhoneAndCreatedAt(String phone, Date createdAt) {
        return orderDAO.getByUserPhoneAndCreatedAt(phone, createdAt);
    }

//    public Order getOrderByCreatedAt(Date createdAt) {
//        return orderDAO.getByCreatedAt(createdAt);
//    }

    private Date shiftTimeZone(Date date, TimeZone sourceTimeZone, TimeZone targetTimeZone) {
        Calendar sourceCalendar = Calendar.getInstance();
        sourceCalendar.setTime(date);
        sourceCalendar.setTimeZone(sourceTimeZone);

        Calendar targetCalendar = Calendar.getInstance();
        for (int field : new int[]{Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND}) {
            targetCalendar.set(field, sourceCalendar.get(field));
        }
        targetCalendar.setTimeZone(targetTimeZone);

        return targetCalendar.getTime();
    }


}
