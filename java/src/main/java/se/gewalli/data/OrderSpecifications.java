package se.gewalli.data;

import com.querydsl.jpa.impl.JPAQuery;

import se.gewalli.entities.Order;
import se.gewalli.entities.QOrder;

public class OrderSpecifications {

    public static JPAQuery<Order> getOrderWithCustomerLastname(JPAQuery<Order> query, String lastname) {
        QOrder order = QOrder.order;
        return query.from(order).where(order.customer.lastname.eq(lastname));
    }
}
