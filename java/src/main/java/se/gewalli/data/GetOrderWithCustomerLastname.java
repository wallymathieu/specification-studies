package se.gewalli.data;

import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQuery;

import se.gewalli.entities.Order;
import se.gewalli.entities.QOrder;

public class GetOrderWithCustomerLastname {
    
    private EntityManager em;

    public GetOrderWithCustomerLastname(EntityManager em) {
        this.em = em;
    }

    public JPAQuery<Order> toQuery(String lastname) {
        JPAQuery<Order> query = new JPAQuery<>(em);
        QOrder order= QOrder.order;
        return query.from(order).where(order.customer.lastname.eq("Bohlen"));
    }
}
