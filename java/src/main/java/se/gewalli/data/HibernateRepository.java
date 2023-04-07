package se.gewalli.data;

import org.hibernate.Session;
import se.gewalli.entities.Customer;
import se.gewalli.entities.Order;
import se.gewalli.entities.Product;

import java.util.Collection;
import java.util.Optional;

public class HibernateRepository extends Repository  {
    private final Session session;
    public HibernateRepository(Session session){
        this.session=session;
    }
    @Override
    public Optional<Customer> tryGetCustomer(int customerId) {
        return session.byId(Customer.class).loadOptional(customerId);
    }

    @Override
    public Optional<Product> tryGetProduct(int productId) {
        return session.byId(Product.class).loadOptional(productId);
    }

    @Override
    public Optional<Order> tryGetOrder(int orderId) {
        return session.byId(Order.class).loadOptional(orderId);
    }

    @Override
    public void save(Product product) {
        session.saveOrUpdate(product);
    }

    @Override
    public void save(Order order) {
        session.saveOrUpdate(order);
    }

    @Override
    public void save(Customer customer) {
        session.saveOrUpdate(customer);
    }

    @Override
    public Collection<Customer> getCustomers() {
        return session.createQuery( "from Customers", Customer.class).getResultList();
    }

    @Override
    public Collection<Product> getProducts() {
        return session.createQuery( "from Products", Product.class).getResultList();
    }

    @Override
    public Collection<Order> getOrders() {
        return session.createQuery( "from Orders", Order.class).getResultList();
    }
}
